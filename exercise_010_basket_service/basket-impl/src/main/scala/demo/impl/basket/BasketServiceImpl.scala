package demo.impl.basket

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import demo.api.basket.{Basket, BasketService, Item}

import scala.concurrent.{ExecutionContext, Future}

class BasketServiceImpl(persistentEntities: PersistentEntityRegistry)(implicit ec: ExecutionContext)
  extends BasketService {
  private var baskets = Map[String, Basket]()

  override def getBasket(basketId: String): ServiceCall[NotUsed, Basket] = ServiceCall { req =>
    baskets.synchronized {
      Future.successful(baskets.getOrElse(basketId, Basket(Seq(), 0)))
    }
  }

  override def addItem(basketId: String): ServiceCall[Item, NotUsed] = ServiceCall { item =>
    baskets.synchronized {
      val newItems = baskets.get(basketId).toSeq.flatMap(_.items) :+ item
      baskets  = baskets.+(basketId -> Basket(newItems, newItems.map(_.price).sum))
    }
    Future.successful(NotUsed)
  }
}
