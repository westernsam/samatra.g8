package $package$

import com.springer.samatra.extras.logging.NoOpRequestLog
import com.springer.samatra.extras.metrics.{GzippedMetricsEnabledHandler, JvmMetricsCollector, JvmMetricsCollectorStatsDService, MetricsStatsdClient}
import com.springer.samatra.extras.responses.{MustacheRenderer, TemplateRenderer}
import com.springer.samatra.extras.{Logger, RoutePrinting, WebServer, WebappContextHandler}
import com.timgroup.statsd.{NonBlockingStatsDClient, StatsDClient}
import org.asynchttpclient.{AsyncHttpClient, DefaultAsyncHttpClient, DefaultAsyncHttpClientConfig}
import org.eclipse.jetty.server.Slf4jRequestLog
import org.eclipse.jetty.server.handler.RequestLogHandler

import scala.concurrent.ExecutionContext

object WebApp extends Logger {

  private val templateReader = if (isLocal)
    new MustacheRenderer.FileTemplateLoader("src/main/resources/templates")
  else
    new MustacheRenderer.ClasspathTemplateLoader("/templates")

  implicit val renderer: TemplateRenderer = new MustacheRenderer(Map(), templateReader, !isLocal)
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  val statsd: StatsDClient = new NonBlockingStatsDClient(s"services.$package$.cf.\${sys.env("ENV")}",
    sys.env.getOrElse("STATSD_HOST", "localhost"),
    sys.env.getOrElse("STATSD_PORT", "8080").toInt)

  val jvmstats = new JvmMetricsCollectorStatsDService(new JvmMetricsCollector, statsd, sys.env.getOrElse("CF_INSTANCE_INDEX", "LOCAL"))
  val statsdClient = MetricsStatsdClient.newStatsDClient(statsd, sys.env("ENV"))

  val http: AsyncHttpClient = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
    .setIoThreadsCount(1)
    .setKeepAlive(true)
    .setConnectTimeout(1000)
    .setRequestTimeout(2000)
    .setMaxConnectionsPerHost(20)
    .setFollowRedirect(true).build()
  )

  def apply(port: Int = 0) = new WebServer(port)
    .addHandler(new GzippedMetricsEnabledHandler(new WebappContextHandler with RoutePrinting {}
      .addRoutes("/example/*",
        new TemplateExampleController(),
        new ProxyExampleController(http)
      )
      .printRoutesTo(this), statsdClient)
    )
    .addHandler(new RequestLogHandler {
      setRequestLog(if (isLocal) new Slf4jRequestLog else new NoOpRequestLog)
    })
    .addErrorHandler(new LogErrors()) //customize error pages

  def isLocal: Boolean = sys.env.getOrElse("ENV", "LOCAL") == "LOCAL"

}
