package $package$

import com.springer.samatra.extras.core.templating.{TemplateRenderer, TemplateResponse}
import com.springer.samatra.extras.mustache.MustacheRenderer
import com.springer.samatra.testing.unit.ControllerTestHelpers._
import org.scalatest.FunSpec

import scala.concurrent.ExecutionContext
import org.scalatest.Matchers._

class ExampleUnitTest extends FunSpec {

  implicit val ec : ExecutionContext = ExecutionContext.global
  implicit val mr : TemplateRenderer = new MustacheRenderer(Map.empty, new MustacheRenderer.ClasspathTemplateLoader("/templates"), false)

  it("should return templated result") {
    val resp = new TemplateExampleController().get("/templated?a=b")

    unwrapFutureResp(resp) shouldBe TemplateResponse("example", Map("params" -> List(Map("key" -> "a", "value" -> "b"))))

    val (status, headers, cookies, body)  = resp.run()

    status shouldBe 200
    new String(body) shouldBe "<html>\n<body>\n<h1>Hi there</h1>\n<p>Keys and values were: </p>\n<ul>\n        <li>a = b</li>\n</ul>\n</body>\n</html>"    
  }
}
