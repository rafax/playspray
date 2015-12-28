package controllers

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import play.api.mvc._

import scala.concurrent.ExecutionContext

object Application extends Controller {

  import play.api.libs.concurrent.Execution.Implicits._

  val bench = new BenchService {
    override implicit def ec: ExecutionContext = scala.concurrent.ExecutionContext.global
  }
  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  def fib(n: Int) = Action.async { implicit request =>
    bench.fib(n) map {
      fib => Ok(s"Fin #${n} = ${fib}")
    }
  }

  def sleep(n: Int) = Action.async { implicit request =>

    bench.sleep(n) map {
      res =>
        Ok(res)
    }
  }
}