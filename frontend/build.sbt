enablePlugins(ScalaJSPlugin)
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

name := "scalable-client"
scalaVersion := "2.12.4"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true
scalaJSModuleKind := ModuleKind.CommonJSModule
skip in packageJSDependencies := false
jsDependencies += "org.webjars" % "firebase" % "3.6.4" / "firebase.js"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.3"
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "1.1.1"
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "extra" % "1.1.1"
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "test" % "1.1.1"
libraryDependencies += "com.github.japgolly.microlibs" %%% "test-util" %  "1.5"
libraryDependencies += "com.olvind" %%% "scalajs-react-components" % "0.8.0"
libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"
libraryDependencies += "io.circe" %%% "circe-core" % "0.8.0"
libraryDependencies += "io.circe" %%% "circe-parser" % "0.8.0"
libraryDependencies += "io.circe" %%% "circe-generic" % "0.8.0"
libraryDependencies += "io.circe" %%% "circe-generic-extras" % "0.8.0"
libraryDependencies += "io.circe" %% "circe-optics" % "0.8.0"
libraryDependencies += "io.suzaku" %%% "diode" % "1.1.2"
libraryDependencies += "io.suzaku" %%% "diode-devtools" % "1.1.2"
libraryDependencies += "io.suzaku" %%% "diode-react"% "1.1.2"
libraryDependencies += "biz.enef" %%% "slogging" % "0.6.1"

