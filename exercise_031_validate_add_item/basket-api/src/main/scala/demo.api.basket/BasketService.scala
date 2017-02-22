package demo.api.basket

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.transport._
import play.api.libs.json.{Format, Json}

case class Item(name: String, price: Int)
object Item {
  implicit val itemFormat: Format[Item] = Json.format
}

case class Basket(items: Seq[Item], total: Int)
object Basket {
  implicit val basketFormat: Format[Basket] = Json.format
}

case class OrderPlaced(basketId: String, basket: Basket)
object OrderPlaced {
  implicit val orderPlacedFormat: Format[OrderPlaced] = Json.format
}

trait BasketService extends Service {
  def getBasket(basketId: String): ServiceCall[NotUsed, Basket]
  def getTotal(basketId: String): ServiceCall[NotUsed, Int]
  def addItem(basketId: String): ServiceCall[Item, NotUsed]
  def clearAll(basketId: String): ServiceCall[NotUsed, NotUsed]
  def placeOrder(basketId: String): ServiceCall[NotUsed, NotUsed]
  def placedOrders: Topic[OrderPlaced]

  override def descriptor: Descriptor = {
    import Method._
    import Service._

    named("basket").withCalls(
      restCall(GET, "/basket/:basketId", getBasket _),
      restCall(GET, "/basket/:basketId/total", getTotal _),
      restCall(POST, "/basket/:basketId/items", addItem _),
      restCall(DELETE, "/basket/:basketId/items", clearAll _),
      restCall(POST, "/basket/:basketId/order", placeOrder _)
    ).withTopics(
      topic("placed-orders", placedOrders)
    )
  }
}
