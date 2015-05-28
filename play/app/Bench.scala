package controllers

import akka.actor.Actor
import controllers.BenchActor.{NthFib, CalculateFib, Sleep}

import scala.concurrent.{Future, ExecutionContext}

object BenchActor {

  case class Sleep(howLong: Long)

  case class CalculateFib(n: Int)

  case class NthFib(n: Int, fib: Long)

}

class BenchActor extends Actor with BenchService {

  override implicit def ec: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def receive = {
    case Sleep(howLong) => val replyTo = sender()
      sleep(howLong) onSuccess {
        case res => replyTo ! res
      }
    case CalculateFib(n: Int) =>
      val replyTo = sender()
      fib(n) onSuccess {
        case res => replyTo ! NthFib(n, res)
      }
  }
}

trait BenchService {

  implicit def ec: ExecutionContext

  def fib(n: Long): Future[Long] =
    if (n <= 2) {
      Future {
        1
      }
    } else {
      for (n1 <- fib(n - 1); n2 <- fib(n - 2)
      ) yield {
        n1 + n2
      }
    }

  def sleep(howLong: Long) = {
    scala.concurrent.Future {
      Thread.sleep(howLong)
      "Slept for " + howLong
    }
  }
}