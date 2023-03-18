ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "company-defense",
    libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R28",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    //Testfx
    libraryDependencies += "org.testfx" % "testfx-junit5" % "4.0.16-alpha" % Test,
    //Junit
    libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.7.0-M1" % Test,
    libraryDependencies += "org.junit.jupiter" % "junit-jupiter-engine" % "5.7.0-M1" % Test,
    libraryDependencies += "org.junit.platform" % "junit-platform-runner" % "1.7.0-M1" % Test
  )

wartremoverErrors ++= Seq(
  Wart.NoNeedImport, 
  Wart.Enumeration,
  Wart.JavaConversions,
  Wart.Null,
  Wart.StringPlusAny
)