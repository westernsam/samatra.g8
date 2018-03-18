package $package$

import com.springer.samatra.extras.responses.{MustacheRenderer, TemplateRenderer, TemplateResponse}
import com.springer.samatra.testing.unit.ControllerTestHelpers._
import org.scalatest.FunSpec
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext
import org.scalatest.Matchers._

class ExampleUnitTest extends FunSpec with ScalaFutures {

  implicit val ec : ExecutionContext = ExecutionContext.global
  implicit val mr : TemplateRenderer = new MustacheRenderer(Map.empty, new MustacheRenderer.ClasspathTemplateLoader("/templates"), false)

  it("should return templated result") {
    val result = new TemplateExampleController().get("/templated?a=b"))

    resp shouldBe TemplateResponse("example", Map("params" -> List(Map("key" -> "a", "value" -> "b"))))

    val (status, headers, cookies, body)  = resp.run()

    status shouldBe 200
    new String(body) shouldBe "<html>\n<body>\n<h1>Hi there</h1>\n<p>Keys and values were: </p>\n<ul>\n        <li>a = b</li>\n</ul>\n</body>\n</html>"
    }
  
}
