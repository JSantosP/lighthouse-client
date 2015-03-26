package lighthouse

import scala.util.Try
import scala.concurrent.Await
import scala.concurrent.duration._
import spray.http._
import spray.client.pipelining._
import akka.actor.ActorSystem
import lighthouse.utils.Loggable
import lighthouse.model._
import spray.httpx.SprayJsonSupport._
import Client._

case class Client(url: URL, timeoutSeconds: Int = DefaultTimeout) extends Loggable {

  implicit private val system = ActorSystem()

  import system.dispatcher

  private val pipeline = sendReceive

  def get(key: Key): Option[ResourceValue] = {
    Try {
      val HttpResponse(status, entity, _, _) =
        Await.result(
          pipeline(Get(s"$url$key")), timeoutSeconds.seconds)
      if (status.intValue == 200)
        Some(resourceValueFormat.read(entity.asString))
      else {
        log.error(s"Couldn't get $key. Status code [${status.intValue}]")
        None
      }
    }.recover {
      case t: Throwable =>
        log.error(s"Couldn't get key $key due to [${t.getMessage}}]")
        None
    }.get
  }

  def set(key: Key, value: ResourceValue): Try[Unit] = Try {
    Try {
      val HttpResponse(status, _, _, _) =
        Await.result(
          pipeline(Put(s"$url$key", value).withHeaders()), timeoutSeconds.seconds)
      status.intValue match {
        case 200 => ()
        case code => throw new IllegalArgumentException(
          s"Couldn't set key $key with value $value (Status code [$code])")
      }
    }
  }

  def close(): Unit =
    system.shutdown()

}

object Client {

  type URL = String
  type Key = String

  val DefaultTimeout = 3

  //seconds

  implicit class dsl(client: Client) {

    def ?(key: Key): Option[ResourceValue] =
      client.get(key)

    def set(key: Key) = new {
      def withValue(value: ResourceValue): Try[Unit] =
        client.set(key, value)
    }

  }

}