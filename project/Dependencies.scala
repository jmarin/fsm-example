import sbt._

object Dependencies {
  val repos = Seq(
    "Local Maven Repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases"
  )

  val logback = "ch.qos.logback" % "logback-classic" % "1.4.7"
  val scalaTest =
    "org.scalatest" %% "scalatest" % Version.scalaTest % "it, test"
  val scalaCheck =
    "org.scalacheck" %% "scalacheck" % Version.scalaCheck % "it, test"

  val catsEffect = "org.typelevel" %% "cats-effect" % "3.4.9"
  val log4Cats = "org.typelevel" %% "log4cats-slf4j" % "2.6.0"
}
