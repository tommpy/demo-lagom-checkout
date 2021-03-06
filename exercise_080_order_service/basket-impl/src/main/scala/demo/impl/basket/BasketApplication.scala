package demo.impl.basket

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import demo.api.basket.{Basket, BasketService}
import play.api.libs.ws.ahc.AhcWSComponents

import scala.collection.immutable.Seq

abstract class BasketApplication(ctx: LagomApplicationContext) extends LagomApplication(ctx)
  with AhcWSComponents
  with LagomKafkaComponents
  with CassandraPersistenceComponents {
  override def lagomServer: LagomServer = LagomServer.forServices {
    bindService[BasketService].to(wire[BasketServiceImpl])
  }


  persistentEntityRegistry.register(wire[BasketEntity])
}

class BasketApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new BasketApplication(context) with LagomDevModeComponents {
      override def jsonSerializerRegistry: JsonSerializerRegistry = BasketSerializerRegistry
    }

  override def load(context: LagomApplicationContext): LagomApplication = new BasketApplication(context) {
    override def serviceLocator: ServiceLocator = NoServiceLocator
    override def jsonSerializerRegistry: JsonSerializerRegistry = BasketSerializerRegistry
  }
}

object BasketSerializerRegistry extends JsonSerializerRegistry {
  import BasketEntityFormats._
  override def serializers: Seq[JsonSerializer[_]] = Seq (
    JsonSerializer[AddItem],
    JsonSerializer[GetBasket.type],
    JsonSerializer[GetTotal.type],
    JsonSerializer[ClearAll.type],
    JsonSerializer[BasketCleared.type],
    JsonSerializer[ItemAdded],
    JsonSerializer[BasketEntityState],
    JsonSerializer[Basket],
    JsonSerializer[OrderPlaced],
    JsonSerializer[PlaceOrder.type],
    JsonSerializer[Basket]
  )
}