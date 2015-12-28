package controllers

import scala.concurrent.{Future, ExecutionContext}

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