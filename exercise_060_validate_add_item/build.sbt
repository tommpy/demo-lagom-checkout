organization in ThisBuild := "sample.helloworld"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "3.3"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

val step = "060"

lazy val basketApi = project("basket-api")
  .settings(version := "1.0-SNAPSHOT")
  .settings(
    libraryDependencies += lagomScaladslApi
  )

lazy val basketImpl = project("basket-impl")
  .settings(version := "1.0-SNAPSHOT")
  .dependsOn(basketApi)
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      macwire,
      lagomScaladslKafkaBroker,
      lagomScaladslApi,
      lagomScaladslPersistenceCassandra,
      scalaTest,
      lagomScaladslTestKit)
  )

def project(id: String) = Project(s"${id}_$step", base = file(id))
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))
  .settings(jacksonParameterNamesJavacSettings: _*)


// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)

aggregateProjects(basketApi, basketImpl)