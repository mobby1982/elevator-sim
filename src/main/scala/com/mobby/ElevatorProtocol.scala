
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
  case class ElevatorStats(name: String, currentFloor: Int, numOfReqProcessed: Long)
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
    implicit val elformat = jsonFormat3(ElevatorStats.apply)
  }

  object CollectionElevatorStats extends DefaultJsonProtocol {
    implicit val cesFormat = jsonFormat1(CollectionElevatorStats.apply)
  }
}



object elevatorProtocol extends ElevatorProtocol
