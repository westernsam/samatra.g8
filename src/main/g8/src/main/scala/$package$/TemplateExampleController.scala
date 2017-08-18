package $package$

import com.springer.samatra.extras.responses.{TemplateRenderer, TemplateResponse}
import com.springer.samatra.routing.FutureResponses.Implicits.fromFuture
import com.springer.samatra.routing.Routings.Controller

import scala.concurrent.{ExecutionContext, Future}


class TemplateExampleController(implicit r: TemplateRenderer, ec: ExecutionContext) extends Controller {

  get("/templated") { req =>
    Future {
      TemplateResponse("example",
        Map("params" -> req.queryStringMap.map {
          case (k, v) => Map("key" -> k, "value" -> v.mkString(","))
        })
      )
    }
  }
}
