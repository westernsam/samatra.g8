package $package$

import java.nio.ByteBuffer

import com.springer.samatra.routing.FutureResponses.Implicits.fromFuture
import com.springer.samatra.routing.FutureResponses.fromFutureWithTimeout
import com.springer.samatra.routing.Routings.Controller
import com.springer.samatra.routing.StandardResponses.Halt
import com.springer.samatra.extras.ApplicationError
import com.springer.samatra.extras.responses.JettySpecificResponses.fromByteBuffer
import com.springer.samatra.extras.responses.{TemplateRenderer, TemplateResponse}
import dispatch._

import scala.concurrent.{ExecutionContext, Future}


class ExampleController(httpClient: Http)(implicit ex: ExecutionContext, r: TemplateRenderer) extends Controller {

  get("/templated") { req =>
    Future {
      TemplateResponse("example",
        Map("params" -> req.queryStringMap.map {
          case (k, v) => Map("key" -> k, "value" -> v.mkString(","))
        })
      )
    }
  }

  get("/proxy/(.*)".r) { req =>
    fromFutureWithTimeout(2000, {
      val request: Future[Res] = httpClient(url(s"http://\${req.captured(0)}"))
      for {
        googleInTheFuture <- request.either
      } yield {
        for {
          responseOrErr <- googleInTheFuture
        } yield responseOrErr.getResponseBodyAsByteBuffer
      } match {
        case Right(bb: ByteBuffer) => fromByteBuffer(bb)
        case Left(StatusCode(code)) => Halt(code)
        case Left(err) => Halt(500, Some(ApplicationError(err)))
      }
    })
  }
}
