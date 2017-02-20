package demo.impl.basket

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import demo.api.basket.{Basket, Item}
import play.api.libs.json.{Format, Json}

object BasketEntityFormats {
  implicit val addItemFormat: Format[AddItem] = Json.format
  implicit val getBasketFormat: Format[GetBasket.type] = JsonSerializer.emptySingletonFormat(GetBasket)
  implicit val getTotalFormat: Format[GetTotal.type] = JsonSerializer.emptySingletonFormat(GetTotal)
  implicit val itemAddedFormat: Format[ItemAdded] = Json.format
  implicit val basketEntityStateFormat: Format[BasketEntityState] = Json.format
}

trait BasketEntityCommand
case class AddItem(item: Item) extends BasketEntityCommand with ReplyType[Done]
case object GetBasket extends BasketEntityCommand with ReplyType[Basket]
case object GetTotal extends BasketEntityCommand with ReplyType[Int]

case class BasketEntityState(currentBasket: Basket)
object BasketEntityState{
  val initial = BasketEntityState(Basket(Seq(), 0))
}

sealed trait BasketEntityEvent extends AggregateEvent[BasketEntityEvent] {
  override def aggregateTag = BasketEntityEvent.Tag
}

object BasketEntityEvent {
  val Tag = AggregateEventTag.sharded[BasketEntityEvent](4)
}

case class ItemAdded(item: Item) extends BasketEntityEvent

final class BasketEntity extends PersistentEntity {
  override type Command = BasketEntityCommand
  override type Event = BasketEntityEvent
  override type State = BasketEntityState

  override def initialState = BasketEntityState.initial

  override def behavior: Behavior = {
    Actions()
      .onCommand[AddItem, Done] {
        case (AddItem(item), ctx, state) =>
          ctx.thenPersist(ItemAdded(item))(_ => ctx.reply(Done))
      }
      .onReadOnlyCommand[GetBasket.type, Basket] {
        case (GetBasket, ctx, state) => ctx.reply(state.currentBasket)
      }
      .onReadOnlyCommand[GetTotal.type, Int] {
        case (GetTotal, ctx, state) => ctx.reply(state.currentBasket.total)
      }
      .onEvent {
        case (ItemAdded(item), state) => {
          val newItems = state.currentBasket.items :+ item
          state.copy(Basket(newItems, newItems.map(_.price).sum))
        }
      }
  }
}
