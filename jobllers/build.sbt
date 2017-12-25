organization in ThisBuild := "com.jobllers"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lazy val `jobllers` = (project in file("."))
  .aggregate(`job-listing-api`, `job-listing-impl`,`jobs-scrapper-impl`,`jobs-scrapper-api`, `models`, `common`)


lazy val `common` = (project in file("common"))
  .settings(
    libraryDependencies ++= Seq(
      json
    ))

lazy val `models` = (project in file("models"))
  .settings(
    libraryDependencies ++= Seq(
      json
    ))
  .dependsOn(`common`)

/*lazy val `web-module` = (project in file("web-module"))
  .enablePlugins(PlayScala && LagomPlay)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire,
      scalaTest
    ))
  .dependsOn(`models`)*/

lazy val `job-listing-api` = (project in file("job-listing-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    ))
  .dependsOn(`models`)

lazy val `job-listing-impl` = (project in file("job-listing-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire,
      scalaTest
    ))
  .dependsOn(`models`, `job-listing-api`)

lazy val `jobs-scrapper-api` = (project in file("jobs-scrapper-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    ))
  .dependsOn(`models`)

lazy val `jobs-scrapper-impl` = (project in file("jobs-scrapper-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire,
      scalaTest,
      "org.jsoup" % "jsoup" % "1.11.2"
    ))
  .dependsOn(`models`, `jobs-scrapper-api`)

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lagomUnmanagedServices in ThisBuild += ("elastic-search" -> "http://127.0.0.1:9200")
