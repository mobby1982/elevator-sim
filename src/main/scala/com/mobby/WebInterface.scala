package com.mobby

import akka.actor.{PoisonPill, Props, Actor, ActorLogging}
import akka.util.Timeout
import spray.http.{StatusCodes, StatusCode}
import spray.routing.{HttpServiceActor, RequestContext, Route, HttpService}
import scala.concurrent.duration._
import scala.language.postfixOps

class ElevatorApp extends HttpServiceActor with WebInterface {
  def receive = runRoute(routes)
}

trait WebInterface extends HttpService with ActorLogging { self: Actor =>

  import context._
  import elevatorProtocol._

  implicit val timeout = Timeout(5 seconds)
  import akka.pattern.ask
  import akka.pattern.pipe

  val elevatorCordinator = context.actorOf(Props[ElevatorCordinator], "ElevatorController")

  def routes: Route = {

    path("start") {
      get { requestCtx =>
        val responder = createResponder(requestCtx)
        elevatorCordinator.ask(CreateElevator).pipeTo(responder)
      }
    } ~
    path("stats"){
      get { requestCtx =>
        val responder = createResponder(requestCtx)
        elevatorCordinator.ask(CollectStats).pipeTo(responder)

      }
    }
  }

  def createResponder(requestCtx: RequestContext) = {
    context.actorOf(Props(new Responder(requestCtx)))
  }
}

class Responder(requestContext: RequestContext) extends Actor with ActorLogging {
  import elevatorProtocol._
  import spray.httpx.SprayJsonSupport._
  import elevatorProtocol.ElevatorStats._
  import elevatorProtocol.CollectionElevatorStats._


  def receive = {
    case ElevatorsCreated =>
      requestContext.complete(StatusCodes.OK)
      sender ! PoisonPill

    case CollectionElevatorStats(stats) =>
      requestContext.complete(StatusCodes.OK, stats)
      sender ! PoisonPill
  }
}