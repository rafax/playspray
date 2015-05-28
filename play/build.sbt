name := """playspray-play"""

version := "1.0-SNAPSHOT"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  //"com.kenshoo" %% "metrics-play" % "2.3.0_0.1.9"
)