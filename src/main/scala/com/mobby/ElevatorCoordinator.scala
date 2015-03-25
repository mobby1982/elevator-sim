package com.mobby

import akka.actor._
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.util.Timeout

class ElevatorCoordinator extends Actor with ElevatorCreator with ActorLogging {

  import context._
  implicit val timeout = Timeout(5 seconds)
  import elevatorProtocol._
  import akka.pattern.ask
  private [this] var systemInit = false


  def receive = {
    /*
        limit No of Elevators max to 8, change later, maybe get num from external source
    */
    case CreateElevator =>
      if (systemInit) {
        log.info(s"Elevator system already initialized with elevators")
      } else {

        elevatorNames.foreach {
          name =>
            if (context.child(name).isEmpty){
              createElevator(name)
            }
        }
        sender ! ElevatorsCreated
        self ! SendRequest

      }


    case SendRequest =>
      context.children.foreach { e =>
        e ! FromFloor(generateRequest)
      }

    case Exhausted(name) =>
      log.info(s"Elevator : ${name} is done with its quota.")
      sender ! FromFloor(generateRequest)


    case CollectStats =>
      log.info("Collect stats")
      val caller = sender

      def askForStatsFromEachElevator(elevator : ActorRef) = {
        elevator.ask(GetStats).mapTo[ElevatorStats]
      }

      val futures: Iterable[Future[ElevatorStats]] =
        context.children.map(e => askForStatsFromEachElevator(e))

      Future.sequence(futures).map { stats => caller ! CollectionElevatorStats(stats.toList)}
  }
}

trait ElevatorCreator { self: Actor =>
  def createElevator(name:String) = context.actorOf(Props[Elevator], name)
}
