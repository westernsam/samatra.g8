package $package$


import java.io.Writer
import javax.servlet.http.HttpServletRequest

import com.springer.link.samatra.routing.Request
import com.springer.samatra.extras.Logger
import org.eclipse.jetty.server.handler.ErrorHandler

class LogErrors extends ErrorHandler with Logger {
  override def writeErrorPage(request: HttpServletRequest, writer: Writer, code: Int, message: String, showStacks: Boolean): Unit = {
    val uri = Request(request).toUri

    if (code >= 500)
      log.error(s"Application error: version uri [$uri]", Option(request.getAttribute("javax.servlet.error.exception")).map(_.asInstanceOf[Exception]))

    //write custom error page to writer if required
    super.writeErrorPage(request, writer, code, message, showStacks)
  }
}

