import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import demo.api.basket.{Basket, BasketService, Item}
import demo.impl.basket.{BasketApplication, BasketSerializerRegistry}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

class BasketServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {
  lazy val service = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new BasketApplication(ctx) with LocalServiceLocator {
      override def jsonSerializerRegistry: JsonSerializerRegistry = BasketSerializerRegistry
    }
  }

  override protected def beforeAll() = service
  override protected def afterAll() = service.stop()
  val client = service.serviceClient.implement[BasketService]

  "Basket Service" should {
    "add a single item" in {
      client.addItem("basket1").invoke(Item("Apple", 50)).flatMap { response =>
        response should ===(NotUsed)

        client.getBasket("basket1").invoke().map { getItemsResponse =>
          getItemsResponse should ===(Basket(Seq(Item("Apple", 50)), 50))
        }
      }
    }

    "get an empty basket" in {
      client.getBasket("basket2").invoke().map { getItemsResponse =>
        getItemsResponse should ===(Basket(Seq(), 0))
      }
    }

    "add multiple items" in {
      val items = ("Apple" -> 50 :: "Apple" -> 50 :: "Orange" -> 30 :: Nil).map { t =>
        Item(t._1, t._2)
      }

      items.foldLeft(Future.successful(List[NotUsed]())) { (f, i) =>
        f.flatMap(l => client.addItem("basket3").invoke(i).map(n => n :: l))
      }.flatMap { f =>
        client.getBasket("basket3").invoke().map { getItemsResponse =>
          getItemsResponse.items should be(items)
          getItemsResponse.total should===(130)
        }
      }
    }
  }
}
