package com.mobby

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import spray.can.Http
import scala.language.postfixOps
import akka.pattern.ask

object Main extends App {

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system = ActorSystem("elevator-actor-system")

  val webApi = system.actorOf(Props(new ElevatorApp()), "elevatorApp")

  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(5 seconds)

  IO(Http).ask(Http.Bind(listener = webApi, interface = host, port = port))
  .mapTo[Http.Event]
  .map {
    case Http.Bound(address) =>
      println(s"WebApi bound to ${address}")
    case Http.CommandFailed(cmd) =>
      println("WebApi could not be bind to" +
        s"${host}:${port}, ${cmd.failureMessage}")
      system.shutdown()
  }

}
