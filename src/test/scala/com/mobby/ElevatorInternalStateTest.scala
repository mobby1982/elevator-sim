package com.mobby

import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.{WordSpecLike, MustMatchers}
import elevatorProtocol._
import scala.concurrent.duration._
import akka.pattern.ask

class ElevatorInternalStateTest extends TestKit(ActorSystem("elevatorTestSystem"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with StopSystem {

  implicit val timeout = Timeout(5 seconds)

  "An Elevator" must {
    "change update request buffer when it receives a message" in {
      val elevator = TestActorRef(new Elevator, "e1")
    }
  }
}
