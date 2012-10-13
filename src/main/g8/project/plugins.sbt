libraryDependencies <+= sbtVersion(v => v match {
case "0.11.3" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.3-0.2.11.1"
case x if (x.startsWith("0.12"))  => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
})
