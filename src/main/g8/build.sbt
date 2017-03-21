
name := "$name;format="lower,word"$"

scalaVersion := "2.12.1"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings", "-Xlint")

resolvers += "jitpack" at "https://jitpack.io"
resolvers += ("Local Ivy Repository" at "file:///" + Path.userHome.absolutePath + "/.ivy2/cache")

publish := {}

libraryDependencies ++=
  Seq(
    "net.databinder.dispatch" % "dispatch-core_2.12" % "0.12.0",
    "com.ning" % "async-http-client" % "1.9.33",
    "com.github.springernature" %% "samatra-extras" % "v1.1",
    "ch.qos.logback" % "logback-classic" % "1.1.7",    
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  )

parallelExecution in Test := false

val zip = TaskKey[Unit]("zip", "Creates a deployable artifact.")
zip := {
  val $name;format="lower,word"$Jar = (packageBin in Compile).value
  val libs = ((fullClasspath in Runtime).value.files +++ $name;format="lower,word"$Jar).get pair flatRebase("lib")
  val zipPath = $name;format="lower,word"$Jar.getAbsolutePath.replace(".jar", ".zip")
  val manifest = (baseDirectory.value / "META-INF" ** "*").get pair flatRebase("META-INF")
  IO.zip(libs ++ manifest, file(zipPath))
  streams.value.log.info("Created zip: " + zipPath)
}
