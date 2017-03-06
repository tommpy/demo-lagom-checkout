package demo.api.basket

import com.lightbend.lagom.scaladsl.api.deser._
import com.lightbend.lagom.scaladsl.api.transport._
import play.api.Environment

import scala.util.control._

trait ExtraTransportExceptions {
  case class BadRequest(message: ExceptionMessage) extends TransportException(TransportErrorCode.BadRequest, message) with NoStackTrace
  object BadRequest {
    def apply(message: String): BadRequest = apply(new ExceptionMessage(classOf[BadRequest].getSimpleName, message))
  }
}

object ExtraTransportExceptions extends ExtraTransportExceptions {

  final class CustomExceptionSerializer(environment: Environment) extends DefaultExceptionSerializer(environment) {
    protected override def fromCodeAndMessage(transportErrorCode: TransportErrorCode, exceptionMessage: ExceptionMessage): Throwable = {
      transportErrorCode match {
        case TransportErrorCode.BadRequest ⇒ BadRequest(exceptionMessage)
        case _                             ⇒ super.fromCodeAndMessage(transportErrorCode, exceptionMessage)
      }
    }
  }
}
