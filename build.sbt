import Dependencies._

lazy val scala213 = "2.13.9"
lazy val scala3 = "3.2.1"
lazy val supportedScalaVersions = List(scala213, scala3)

ThisBuild / organization := "com.jmarin"
ThisBuild / scalaVersion := scala3

lazy val `fsm-example` = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    ThisBuild / dynverSonatypeSnapshots := true,
    name := "fsm-example",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      logback,
      scalaTest,
      scalaCheck,
      catsEffect,
      log4Cats
    )
  )
