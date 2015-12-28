package com.example

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import spray.routing._

import scala.concurrent.ExecutionContext

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  import scala.concurrent.ExecutionContext.Implicits.global

  val bench = new BenchService {
    override implicit def ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  }
  
  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  def fib(n: Long): Long = {
    if (n <= 2) {
      1
    } else {
      fib(n - 1) + fib(n - 2)
    }
  }

  val myRoute =
    path("sleep" / Segment) { n =>
      get {
        complete {
          val s = n.toInt
          bench.sleep(s) map {
            f => s"Slept for $s ms"
          }
        }
      }
    } ~
      path("fib" / Segment) { n =>
        get {
          complete {
            bench.fib(n.toInt) map { fib =>
              s"Fin #${n} = ${fib}"
            }
          }
        }
      }
}