package com.mobby

import akka.actor._

class Elevator extends Actor with ActorLogging {
  import elevatorProtocol._

  var request = Vector[RequestElevator]()
  var currentFloor = 0
  var isBusy = false
  val random = new scala.util.Random()
  var numOfReqProcessed  = 0L

  def simulateTimePassage(i: Int) =   Thread.sleep(i * TIME_DELTA_IN_MILLS)

  def receive = {

    case FromFloor(req) => 
      request = request ++ req
      self ! ProcessRequest
      
    case ProcessRequest =>
      if (request.nonEmpty) {
        val RequestElevator(fromFloor, direction) = request.head
        request = request.tail
        isBusy = true
        val distance = math.max(fromFloor, currentFloor)
        /*
            simulate passage of time
        */
        simulateTimePassage(distance)
        currentFloor = fromFloor
        self ! RequestFloor(random.nextInt(100))
        
      } else {
        isBusy = false
        /*
            ask for more reqs
         */
        context.parent ! Exhausted(self.path.name)
      }

    case RequestFloor(floorNo) =>
      log.info(s"Elevator : ${self.path.name} is going to floor : ${floorNo}")
      val distance = math.max(floorNo, currentFloor)
      simulateTimePassage(distance)

      currentFloor = floorNo
      numOfReqProcessed = numOfReqProcessed + 1
      self ! ProcessRequest

    case GetStats =>
      sender ! ElevatorStats(self.path.name, currentFloor, numOfReqProcessed)
  }

}
