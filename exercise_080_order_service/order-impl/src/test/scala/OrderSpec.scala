import akka.Done
import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import com.typesafe.config.ConfigFactory
import demo.impl.order._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.concurrent.Await

class OrderSpec extends WordSpecLike with Matchers with BeforeAndAfterAll with TypeCheckedTripleEquals {
  val system = ActorSystem("PostSpec", JsonSerializerRegistry.actorSystemSetupFor(OrderSerializerRegistry))

  override protected def afterAll() {
    Await.ready(system.terminate(), 20.seconds)
  }

  "Order" must {
    "be created" in {
      val driver = new PersistentEntityTestDriver(system, new Order, "order1")
      val items = IndexedSeq("Apple", "Banana")
      val outcome = driver.run(CreateOrder(items))
      outcome.issues should ===(List())
      outcome.state should ===(OrderState(items))
      outcome.events should ===(List(OrderInitialized(items)))
      outcome.replies should ===(List(Done))

      val getOutcome = driver.run(GetOrder)

      getOutcome.issues should ===(List())
      getOutcome.state should ===(OrderState(items))
      getOutcome.replies should ===(List(OrderItems(items)))
    }
  }
}
