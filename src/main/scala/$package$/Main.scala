package $package$

import com.springer.samatra.extras.logging.EnvConfiguredLogging

object Main extends EnvConfiguredLogging { 

  def main(args: Array[String]): Unit = {
    WebApp.jvmstats.initialise()

    WebApp(sys.env("PORT").toInt).startAndWait()
  }
}
