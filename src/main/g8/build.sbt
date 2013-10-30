name :="spray-demo"

version :="0.1"

scalaVersion :="2.10.3"

seq(webSettings :_*)

conflictWarning := ConflictWarning.disable

resolvers +="spray repo" at "http://repo.spray.io/"

libraryDependencies ++=Seq(
"org.mortbay.jetty" % "jetty" % "6.1.26" % "container",
"ch.qos.logback" % "logback-classic" % "1.0.13" % "runtime",
"io.spray" %  "spray-routing"    % "1.2-RC2"   % "compile",
"io.spray" %  "spray-can"    % "1.2-RC2"   % "compile",
"com.typesafe.akka" %% "akka-actor" % "2.2.3" ,
"com.chuusai" % "shapeless_2.10.2" % "2.0.0-M1"

)
