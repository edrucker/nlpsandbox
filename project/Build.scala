import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "nlpsandbox"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
    // "org.apache.opennlp" % "opennlp-tools" % "1.5.2-incubating"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here  
  )

}
