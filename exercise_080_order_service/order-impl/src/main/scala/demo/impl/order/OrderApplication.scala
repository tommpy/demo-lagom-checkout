package demo.impl.order

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire.wire
import demo.api.basket.BasketService
import demo.api.order.OrderService
import play.api.libs.ws.ahc.AhcWSComponents

import scala.collection.immutable.Seq

abstract class OrderApplication(ctx: LagomApplicationContext) extends LagomApplication(ctx)
  with AhcWSComponents
  with CassandraPersistenceComponents
  with LagomKafkaComponents {
  override def lagomServer: LagomServer = LagomServer.forServices {
    bindService[OrderService].to(wire[OrderServiceImpl])
  }

  lazy val basketService: BasketService = serviceClient.implement[BasketService]

  persistentEntityRegistry.register(wire[Order])

  wire[BasketServiceSubscriber]
}

class OrderApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    new OrderApplication(context) with LagomDevModeComponents {
      override def jsonSerializerRegistry: JsonSerializerRegistry = OrderSerializerRegistry
    }
  }

  override def load(context: LagomApplicationContext): LagomApplication = {
    new OrderApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
      override def jsonSerializerRegistry: JsonSerializerRegistry = OrderSerializerRegistry
    }
  }
}

object OrderSerializerRegistry extends JsonSerializerRegistry {
  import OrderEntityFormats._
  override def serializers: Seq[JsonSerializer[_]] = {
    Seq(JsonSerializer[OrderInitialized],
      JsonSerializer[OrderState],
      JsonSerializer[CreateOrder],
      JsonSerializer[GetOrder.type],
      JsonSerializer[OrderItems])
  }
}
