package demo.impl.order

import akka.stream.scaladsl.Flow
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import demo.api.basket.{BasketService, OrderPlaced}

class BasketServiceSubscriber(persistentEntities: PersistentEntityRegistry, basketService: BasketService) {
  basketService.placedOrders.subscribe.atLeastOnce(Flow[OrderPlaced].mapAsync(1) {
    case OrderPlaced(basketId, basket) => {
      val ref = persistentEntities.refFor[Order](basketId)
      ref.ask(CreateOrder(basket.items.map(_.name)))
    }
  })
}
