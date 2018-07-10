package $package$

import com.springer.samatra.extras.core.logging.LogBackEnvConfiguredLogging

object Main extends LogBackEnvConfiguredLogging {
  def main(args: Array[String]): Unit = {
    WebApp.jvmstats.initialise()

    WebApp(sys.env("PORT").toInt).startAndWait()
  }
}
