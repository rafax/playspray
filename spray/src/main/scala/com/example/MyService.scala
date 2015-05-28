package com.example

import java.util.concurrent.TimeUnit

import akka.actor.{Props, Actor}
import akka.util.Timeout
import com.example.BenchActor.{Sleep, NthFib, CalculateFib}
import spray.routing._

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

  
  implicit val timeout = Timeout(10, TimeUnit.SECONDS)
  val bench = actorRefFactory.actorOf(Props[BenchActor])

  def fib(n: Long): Long = {
    if (n <= 2) {
      1
    } else {
      fib(n - 1) + fib(n - 2)
    }
  }

  import akka.pattern.ask

  val myRoute =
    path("sleep" / Segment) { n =>
      get {
        complete {
          val s = n.toInt
          import scala.concurrent.ExecutionContext.Implicits.global
          (bench ? Sleep(s)) map {
            f =>
              s"Slept for $s ms"
          }
        }
      }
    } ~
      path("fib" / Segment) { n =>
        get {
          complete {
            import scala.concurrent.ExecutionContext.Implicits.global
            (bench ? CalculateFib(n.toInt)).mapTo[NthFib] map { f =>
              s"Fin #${f.n} = ${f.fib}"
            }
          }
        }
      }
}