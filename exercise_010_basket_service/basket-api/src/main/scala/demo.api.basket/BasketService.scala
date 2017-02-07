package demo.api.basket

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service}
import com.lightbend.lagom.scaladsl.api.transport._

case class Item(name: String, price: Int)

trait BasketService extends Service {
  override def descriptor: Descriptor = {
    import Method._
    import Service._

    def getItems(basketId: String): ServiceCall[Seq[Item], NotUsed]
    def addItem(basketId: String): ServiceCall[NotUsed, Item]

    named("basket").withCalls(
      restCall(GET, "/basket/:basketId/items", getItems _),
      restCall(POST, "/basket/:basketId/items", getItems _)
    )
  }
}
