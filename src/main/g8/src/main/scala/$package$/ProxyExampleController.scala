package $package$

import java.nio.ByteBuffer


import com.springer.samatra.extras.core.asynchttp.AsyncHttpHelpers.{ListenableFutureOps, StatusCode, ensureOk}
import com.springer.samatra.extras.core.ApplicationError
import com.springer.samatra.extras.core.jetty.JettySpecificResponses.fromByteBuffer
import com.springer.samatra.routing.FutureResponses.fromFutureWithTimeout
import com.springer.samatra.routing.Routings.Controller
import com.springer.samatra.routing.StandardResponses.Halt
import org.asynchttpclient.AsyncHttpClient

import scala.concurrent.ExecutionContext

class ProxyExampleController(httpClient: => AsyncHttpClient)(implicit ec: ExecutionContext) extends Controller {
  get("/proxy/(.*)".r) { req =>
    fromFutureWithTimeout(2000, {
      val request = httpClient.prepareGet(s"\${req.captured(0)}").execute(ensureOk).toFutureEither
      for {
        googleInTheFuture <- request
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
