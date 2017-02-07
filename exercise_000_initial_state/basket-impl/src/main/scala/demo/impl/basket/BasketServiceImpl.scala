package demo.impl.basket

import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import demo.api.basket.BasketService

import scala.concurrent.ExecutionContext

class BasketServiceImpl(persistentEntities: PersistentEntityRegistry)(implicit ec: ExecutionContext)
  extends BasketService {

}
