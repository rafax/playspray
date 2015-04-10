lazy val spray = project.in(file("spray"))

scalacOptions in Compile in spray ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
