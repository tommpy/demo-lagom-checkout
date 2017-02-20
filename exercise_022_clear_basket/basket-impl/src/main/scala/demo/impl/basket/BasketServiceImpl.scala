package demo.impl.basket

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import demo.api.basket.{Basket, BasketService, Item}

import scala.concurrent.ExecutionContext

class BasketServiceImpl(persistentEntities: PersistentEntityRegistry)(implicit ec: ExecutionContext)
  extends BasketService {
  override def getBasket(basketId: String): ServiceCall[NotUsed, Basket] = ServiceCall { req =>
    persistentEntities.refFor[BasketEntity](basketId).ask(GetBasket)
  }

  override def addItem(basketId: String): ServiceCall[Item, NotUsed] = ServiceCall { item =>
    persistentEntities.refFor[BasketEntity](basketId).ask(AddItem(item)).map(_ => NotUsed)
  }

  override def getTotal(basketId: String): ServiceCall[NotUsed, Int] = ServiceCall { req =>
    persistentEntities.refFor[BasketEntity](basketId).ask(GetTotal)
  }

  override def clearAll(basketId: String): ServiceCall[NotUsed, NotUsed] = ServiceCall { req =>
    persistentEntities.refFor[BasketEntity](basketId).ask(ClearAll).map(x => NotUsed)
  }
}