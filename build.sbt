ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "company-defense",
    libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R28"
  )
