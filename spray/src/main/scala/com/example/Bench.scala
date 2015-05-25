package com.example

import akka.actor.Actor
import akka.pattern.{pipe}
import com.example.BenchActor.{NthFib, CalculateFib, Sleep}

import scala.concurrent.{ExecutionContext, Future}

object BenchActor {

  case class Sleep(howLong: Long)

  case class CalculateFib(n: Int)

  case class NthFib(n: Int, fib: Long)

}

class BenchActor extends Actor with BenchService {

  override implicit def ec: ExecutionContext = context.dispatcher

  def receive = {
    case Sleep(howLong) =>
      sleep(howLong) pipeTo (sender())
    case CalculateFib(n: Int) =>
      fib(n) map {
        res => NthFib(n, res)
      } pipeTo (sender())
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