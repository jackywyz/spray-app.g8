name :="spray-demo"

version :="0.1"

scalaVersion :="2.9.2"

seq(webSettings :_*)


resolvers +="spray repo" at "http://repo.spray.cc/"


libraryDependencies ++=Seq("com.typesafe.akka" % "akka-actor" % "2.0.2",
    "com.typesafe.akka" % "akka-slf4j" % "2.0.2",
     "cc.spray"%  "spray-server"    % "1.0-M2.1"   % "compile",
     "org.specs2" % "specs2_2.9.2" % "1.11" % "test",
     "org.eclipse.jetty" % "jetty-webapp" % "8.1.5.v20120716" % "container",
     "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container" artifacts (Artifact("javax.servlet", "jar", "jar"))
)
