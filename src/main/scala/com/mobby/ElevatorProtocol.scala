
package com.mobby

import spray.json.DefaultJsonProtocol

trait ElevatorProtocol {
  
  sealed trait Direction
  case object Up extends Direction
  case object Down extends Direction

  case class RequestElevator(fromFloor: Int, direction: Direction)
  case class FromFloor(reqs: Vector[RequestElevator])
  case class RequestFloor(floor: Int)
  case class Exhausted(elevatorName: String)
  case class ElevatorStats(name: String, currentFloor: Int,
                           numOfReqProcessed: Long, totalDurationInMills: Long,
                           avgTimePerReqMillis: Long)
  case class CollectionElevatorStats(list: List[ElevatorStats])

  case object CreateElevator
  case object ElevatorsCreated
  case object SendRequest
  case object ProcessRequest
  case object CollectStats
  case object GetStats
  /*
   *  Time to move between floors :
   *
   */

  val TIME_DELTA_IN_MILLS = 10

  /*
      JSON serializatoin
   */

  object ElevatorStats extends DefaultJsonProtocol {
    implicit val elformat = jsonFormat5(ElevatorStats.apply)
  }

  object CollectionElevatorStats extends DefaultJsonProtocol {
    implicit val cesFormat = jsonFormat1(CollectionElevatorStats.apply)
  }

  // for now create a Stream of random Request
  def generateRequest = {
    val random = new scala.util.Random()
    (1 to 10).foldLeft(Vector[RequestElevator]())(
      (b,a) => RequestElevator( fromFloor = random.nextInt(100), if (random.nextBoolean) Up else Down) +: b)
  }

  val elevatorNames = (1 to 8).foldLeft(List[String]())((b,a) => ("Elevator-No-" + a) :: b)


}



object elevatorProtocol extends ElevatorProtocol
