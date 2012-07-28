package com.example

import cc.spray._
import http.MediaTypes._
import akka.util.duration._

trait HelloService extends Directives {
  
  val simpleService = {
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          _.complete {
            <html>
              <body>
                <h1>Say hello to <i>spray</i>!</h1>
                <p>Defined resources:</p>
                <ul>
                  <li><a href="/ping">/ping</a></li>
                  <li><a href="/timeout">/timeout</a></li>
                  <li><a href="/2nd">/2nd</a></li>
                </ul>
              </body>
            </html>
          }
        }
      }
    } ~
    path("ping") {
      content(as[Option[String]]) { body =>
        completeWith("PONG! " + body.getOrElse(""))
      }
    } ~
    path("timeout") {
      get { ctx =>
        actorSystem.scheduler.scheduleOnce(1500.millis) {
          ctx.complete("Too late!")
        }
      }
    }
  }

  val secondService = {
    path("2nd") {
      get {
        completeWith("A reply from a second service!")
      }
    }
  }
  
}
