package demo.impl.basket

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.server._
import demo.api.basket.{BasketService, Item}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

import scala.collection.immutable.Seq

abstract class BasketApplication(ctx: LagomApplicationContext) extends LagomApplication(ctx)
  with AhcWSComponents
  with LagomKafkaComponents
  with CassandraPersistenceComponents {
  override def lagomServer: LagomServer = LagomServer.forServices {
    bindService[BasketService].to(wire[BasketServiceImpl])
  }
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
  override def serializers: Seq[JsonSerializer[_]] = Seq (
  )
}