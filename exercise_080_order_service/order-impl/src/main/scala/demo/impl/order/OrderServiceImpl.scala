package demo.impl.order

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import demo.api.order._

import scala.concurrent.ExecutionContext

class OrderServiceImpl(persistentEntities: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends OrderService {
  override def getOrder(orderId: String): ServiceCall[NotUsed, Seq[String]] = ServiceCall { req =>
    persistentEntities.refFor[Order](orderId).ask(GetOrder).map(_.items)
  }
}