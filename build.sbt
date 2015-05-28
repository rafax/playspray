lazy val spray = project.in(file("spray"))

mainClass in assembly := Some("com.example.Main")

scalacOptions in Compile in spray ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
