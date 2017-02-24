import akka.Done
import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.InvalidCommandException
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

      val getPriceOutcome = driver.run(GetTotal)
      getPriceOutcome.issues should ===(Nil)
      getPriceOutcome.replies should ===(List(50))
    }

    "Clear the basket" in {
      val driver = new PersistentEntityTestDriver(system, new BasketEntity, "Basket1")
      val addItemOutcome = driver.run(AddItem(Item("Apple", 50)))

      val clearOutcome = driver.run(ClearAll)
      clearOutcome.issues should ===(Nil)
      clearOutcome.replies should ===(List(Done))

      val getBasketOutcome = driver.run(GetBasket)
      getBasketOutcome.issues should ===(Nil)
      getBasketOutcome.replies should ===(List(Basket(Seq(), 0)))
    }

    "Place an order" in {
      val driver = new PersistentEntityTestDriver(system, new BasketEntity, "Basket2")
      driver.run(AddItem(Item("Apple", 50)))
      val outcome = driver.run(PlaceOrder)
      outcome.issues should ===(List())
      outcome.events should ===(List(OrderPlaced("Basket2", Basket(Seq(Item("Apple", 50)), 50))))
    }

    "Return InvalidCommand if add item is called after the order is placed" in {
      val driver = new PersistentEntityTestDriver(system, new BasketEntity, "Basket3")
      driver.run(AddItem(Item("Apple", 50)))
      driver.run(PlaceOrder)
      val outcome = driver.run(AddItem(Item("Apple", 50)))
      outcome.replies should ===(List(InvalidCommandException("The order has been placed and cannot be modified")))
    }

    "Return InvalidCommand if clear all is called after the order is placed" in {
      val driver = new PersistentEntityTestDriver(system, new BasketEntity, "Basket4")
      driver.run(AddItem(Item("Apple", 50)))
      driver.run(PlaceOrder)
      val outcome = driver.run(ClearAll)
      outcome.replies should ===(List(InvalidCommandException("The order has been placed and cannot be modified")))
    }

    "Return InvalidCommand if order is called on an empty basket" in {
      val driver = new PersistentEntityTestDriver(system, new BasketEntity, "Basket5")
      val outcome = driver.run(PlaceOrder)
      outcome.replies should ===(List(InvalidCommandException("Can't place an order for an empty basket")))
    }
  }
}
