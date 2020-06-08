import de.johoop.jacoco4sbt.JacocoPlugin._
import sbt.Keys._
import sbt.{Configuration, _}
import sbtdoge.CrossPerProjectPlugin

trait BuildSettings {

  import Dependencies._

  val projectName = "atomium"

  def project(moduleName: String): Project = {
    Project(
      id = projectName + "-" + moduleName,
      base = file("modules/" + moduleName),
      settings = projectSettings()
    )
  }


  def mainProject(modules: ProjectReference*): Project = {
    Project(
      id = projectName,
      base = file("."),
      settings = projectSettings()
    ).settings(publishArtifact := false)
      .aggregate(modules: _*)
      .enablePlugins(CrossPerProjectPlugin)
  }

  private def projectSettings() = {

    val projectSettings = Seq(
      crossScalaVersions := Seq("2.12.8", "2.13.2"),
      scalaVersion := "2.12.8",
      parallelExecution := false,
      publishArtifact in Test := true,
      resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
      resolvers += Resolver.sonatypeRepo("public") ,
      resolvers += Resolver.typesafeRepo("releases"),
      libraryDependencies ++= mainDependencies
    )

    Defaults.coreDefaultSettings ++ projectSettings ++  publishSettings ++ jacoco.settings
  }

  val publishSettings = {

    val publishingCredentials = {

      val credentials =
        for {
          username <- Option(System.getenv().get("SONATYPE_USERNAME"))
          password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
        } yield
          Credentials(
            "Sonatype Nexus Repository Manager",
            "oss.sonatype.org",
            username,
            password
          )

      credentials.toSeq
    }


    val pomInfo = {
      <url>https://github.com/WegenenVerkeer/atomium</url>
        <licenses>
          <license>
            <name>MIT licencse</name>
            <url>http://opensource.org/licenses/MIT</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:WegenenVerkeer/atomium.git</url>
          <connection>scm:git:git@github.com:WegenenVerkeer/atomium.git</connection>
        </scm>
        <developers>
          <developer>
            <id>AWV</id>
            <name>De ontwikkelaars van AWV</name>
            <url>http://www.wegenenverkeer.be</url>
          </developer>
        </developers>
    }

    Seq(
      publishMavenStyle := true,
      pomIncludeRepository := { _ => false},
      pomExtra := pomInfo,
      credentials ++= publishingCredentials
    )
  }


}