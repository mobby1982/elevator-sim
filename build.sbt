name := "Elevator"

organization := "com.mobby"

version := "0.0.1"

scalaVersion := "2.11.5"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Spray Repository"    at "http://repo.spray.io")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.9",
  "ch.qos.logback"    %  "logback-classic" % "1.1.2",
  "io.spray"          %% "spray-can"       % "1.3.2",
  "io.spray"          %% "spray-routing"   % "1.3.2",
  "io.spray" %%  "spray-json" % "1.3.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test",
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test" withSources() withJavadoc()
)


