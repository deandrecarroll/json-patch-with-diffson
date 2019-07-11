name := """json-patch-experiment"""
organization := "com.splunk"

version := "1.0-SNAPSHOT"

val jsonLib = "play-json"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.0"

libraryDependencies ++=  Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.gnieh" %% f"diffson-${jsonLib}" % "4.0.0-M3"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.splunk.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.splunk.binders._"
