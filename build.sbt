import sbt.Project.projectToRef

lazy val scalaV = "2.11.8"

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

lazy val scalajsclient = (project in file("scalajs")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "com.lihaoyi" %%% "scalatags" % "0.5.5",
    scalaTest
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .dependsOn(sharedJs)

lazy val clients = Seq(scalajsclient)

lazy val playserver = (project in file("play")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "scalatags" % "0.5.5",
    "org.webjars" % "jquery" % "3.0.0",
    specs2 % Test
  )
).enablePlugins(PlayScala, LagomPlay).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      scalaTest
    )
  ).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val timeApi = commonSettings("time-api")
  .settings(
    scalaVersion := scalaV,
    libraryDependencies += lagomScaladslApi
  )

lazy val timeImpl = commonSettings("time-impl")
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      macwire,
      lagomScaladslTestKit,
      scalaTest
    )
  )
.settings(lagomForkedTestSettings: _*)
.dependsOn(timeApi)

def commonSettings(id: String) = Project(id, base = file(id))
  .settings(eclipseSettings: _*)
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))

lazy val root = (project in file("."))
  .aggregate(playserver, sharedJvm, sharedJs, scalajsclient, timeImpl)

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project root", _: State)) compose (onLoad in Global).value

//Configuration of sbteclipse
//Needed for importing the project into Eclipse
EclipseKeys.skipParents in ThisBuild := false
lazy val eclipseSettings = Seq(
  EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala,
  EclipseKeys.withBundledScalaContainers := false,
  EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
  EclipseKeys.eclipseOutput := Some(".target"),
  EclipseKeys.withSource := true,
  EclipseKeys.withJavadoc := true
)

// do not delete database files on start
lagomCassandraCleanOnStart in ThisBuild := false

fork in run := true
