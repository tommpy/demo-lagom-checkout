package demo.api.basket

import com.lightbend.lagom.scaladsl.api.{Descriptor, Service}
import com.lightbend.lagom.scaladsl.api.transport._

trait BasketService extends Service {
  override def descriptor: Descriptor = {
    import Method._
    import Service._

    named("basket").withCalls(
    )
  }
}
