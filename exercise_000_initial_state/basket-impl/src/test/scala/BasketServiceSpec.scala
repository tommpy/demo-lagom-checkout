import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import demo.api.basket.{BasketService, Item}
import demo.impl.basket.{BasketApplication, BasketSerializerRegistry}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class BasketServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {
  lazy val service = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new BasketApplication(ctx) with LocalServiceLocator {
      override def jsonSerializerRegistry: JsonSerializerRegistry = BasketSerializerRegistry
    }
  }

  override protected def beforeAll() = service
  override protected def afterAll() = service.stop()

  "Basket Service" should {

  }
}
