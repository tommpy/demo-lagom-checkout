package demo.api.order

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.transport._

trait OrderService extends Service {
  def getOrder(orderId: String): ServiceCall[NotUsed, Seq[String]]

  override def descriptor: Descriptor = {
    import Method._
    import Service._

    named("order").withCalls {
      restCall(GET, "/order/:orderId", getOrder _)
    }
  }
}
