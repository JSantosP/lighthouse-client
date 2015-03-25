import NativePackagerKeys._

packageArchetype.java_application

organization := "lighthouse"

name := "lighthouse-client"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies in ThisBuild <++= scalaVersion { (sv: String) =>
  val sprayVersion = "1.3.1"
  val akkaVersion = "2.3.4"
  Seq(
    "lighthouse" %% "lighthouse-server" % "1.0",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "io.spray" %% "spray-client" % sprayVersion
  )
}