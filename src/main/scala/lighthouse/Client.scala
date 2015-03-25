package lighthouse

import scala.util.Try
import scala.concurrent.Await
import scala.concurrent.duration._
import spray.http._
import spray.client.pipelining._
import akka.actor.ActorSystem
import lighthouse.model._
import Client._

case class Client(url: URL, timeoutSeconds: Int = DefaultTimeout) {

  implicit private val system = ActorSystem()
  import system.dispatcher

  private val pipeline = sendReceive

  def get(key: Key): Option[ResourceValue] = {
    val result = Await.result(pipeline(Get(s"${url}$key")), timeoutSeconds.seconds)
    println(result)
    None
  }

  def set(key: Key, value: ResourceValue): Try[Unit] = Try {
    ()
  }

  def close(): Unit =
    system.shutdown()

}

object Client {

  type URL = String
  type Key = String

  val DefaultTimeout = 3 //seconds

  implicit class dsl(client: Client) {

    def ?(key: Key): Option[ResourceValue] =
      client.get(key)

    def <(key: Key) = new {
      def >(value: ResourceValue): Try[Unit] =
        client.set(key, value)
    }

  }

}