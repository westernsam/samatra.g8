package $package$

import com.springer.samatra.routing.Routings.{Controller, Routes}
import com.springer.samatra.routing.StandardResponses.Implicits._
import com.springer.samatra.testing.asynchttp.{InMemoryBackend, ServerConfig}
import org.asynchttpclient.AsyncHttpClient
import org.scalatest.FunSpec

import scala.concurrent.ExecutionContext
import org.scalatest.Matchers._

class ExampleFunctionalTest extends FunSpec with InMemoryBackend {
  implicit val ec: ExecutionContext = ExecutionContext.global

  lazy val http: AsyncHttpClient = client(new ServerConfig {
    mount("/*", Routes(new ProxyExampleController(http)))
    mount("/hello/*", Routes(new Controller {
      get("/yo") { _ =>
        "Result"
      }
    }))
  })

  it("should proxy") {
    http.prepareGet("/proxy//hello/yo").execute().get.getResponseBody shouldBe "Result"
  }
}
