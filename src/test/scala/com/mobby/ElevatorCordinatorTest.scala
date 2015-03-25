package com.mobby

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}
import elevatorProtocol._

class ElevatorCoordinatorTest extends TestKit(ActorSystem("elevatorSystem"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with StopSystem {

  "The Elevator Coordinator" must {
    "Create elevators" in {
      val ec = TestActorRef(new ElevatorCoordinator , "ec")
      ec ! CreateElevator
      var count = 0
      ec.children.foreach { _ => count = count + 1 }
      count must be(8)
      ec ! CreateElevator
      expectMsg(ElevatorsCreated)
    }

    "Collect Stats" in {
      val ec = TestActorRef(new ElevatorCoordinator , "ec1")
      ec ! CollectStats
      expectMsgAllOf[CollectionElevatorStats]()
    }

  }
}
