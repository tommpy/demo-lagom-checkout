package demo.impl.order

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}

object OrderEntityFormats {
  implicit val orderItemsFormat: Format[OrderItems] = Json.format
  implicit val createOrderFormat: Format[CreateOrder] = Json.format
  implicit val getOrderFormat = JsonSerializer.emptySingletonFormat(GetOrder)
  implicit val orderInitialisedFormat: Format[OrderInitialized] = Json.format
  implicit val orderStateFormat: Format[OrderState] = Json.format
}

case class OrderItems (items: Seq[String])

trait OrderCommand
case class CreateOrder(items: Seq[String]) extends OrderCommand with ReplyType[Done]
case object GetOrder extends OrderCommand with ReplyType[OrderItems]

trait OrderEvent
case class OrderInitialized(items: Seq[String]) extends OrderEvent
case class OrderState(items: Seq[String])

object OrderState {
  def empty = OrderState(Seq())
}

class Order extends PersistentEntity {
  override type Command = OrderCommand
  override type Event = OrderEvent
  override type State = OrderState

  override def initialState = OrderState.empty

  override def behavior: Behavior = Actions()
    .onCommand[CreateOrder, Done] {
      case (CreateOrder(items), ctx, state) => {
        ctx.thenPersist(OrderInitialized(items)){
          evt => ctx.reply(Done)
        }
      }
    }
    .onReadOnlyCommand[GetOrder.type, OrderItems] {
      case (GetOrder, ctx, state) => ctx.reply(OrderItems(state.items))
    }
    .onEvent{
      case (OrderInitialized(items), state) => OrderState(items)
    }
}
