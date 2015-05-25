package controllers

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.util.Timeout
import controllers.BenchActor.{CalculateFib, NthFib}
import play.api.mvc._
import play.libs.Akka

object Application extends Controller {

  import play.api.libs.concurrent.Execution.Implicits._

  implicit val timeout = Timeout(10, TimeUnit.SECONDS)
  val myActor = Akka.system.actorOf(Props[BenchActor], name = "myactor")

  def fib(n: Int) = Action.async { implicit request =>
    import akka.pattern.ask

    (myActor ? CalculateFib(n)).mapTo[NthFib] map {
      f =>
        Ok(s"Fin #${f.n} = ${f.fib}")
    }
  }
}