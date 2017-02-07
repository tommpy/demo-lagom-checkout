import akka.Done
import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import demo.api.basket.{Basket, Item}
import demo.impl.basket._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.concurrent.Await

class BasketSpec extends WordSpecLike with Matchers with BeforeAndAfterAll with TypeCheckedTripleEquals {
  val system = ActorSystem("PostSpec", JsonSerializerRegistry.actorSystemSetupFor(BasketSerializerRegistry))

  override protected def afterAll() {
    Await.ready(system.terminate(), 20.seconds)
  }

  "Basket" must {
    "Add an item" in {
      val driver = new PersistentEntityTestDriver(system, new BasketEntity, "Basket1")
      val addItemOutcome = driver.run(AddItem(Item("Apple", 50)))
      addItemOutcome.events should ===(List(ItemAdded(Item("Apple", 50))))
      addItemOutcome.state.currentBasket.total should ===(50)
      addItemOutcome.state.currentBasket.items should ===(IndexedSeq(Item("Apple", 50)))
      addItemOutcome.replies should ===(List(Done))
      addItemOutcome.issues should ===(Nil)

      val getItemsOutcome = driver.run(GetBasket)
      getItemsOutcome.issues should ===(Nil)
      getItemsOutcome.replies should ===(List(Basket(Seq(Item("Apple", 50)), 50)))
    }
  }
}
