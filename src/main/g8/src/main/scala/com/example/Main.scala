package com.example
import scala.concurrent.duration._
import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp
import spray.http._
import MediaTypes._
import util.parsing.json.JSONObject
import spray.routing.authentication._
import scala.concurrent._
import ExecutionContext.Implicits.global
import com.typesafe.config._
case class Emp(val ename:String,val age:Int)

object Main extends App with SimpleRoutingApp {
implicit val system = ActorSystem("simple-routing-app")
 val config = ConfigFactory.load()
//add header:  {"Authorization": "Basic Sm9objpwNHNzdzByZA==", realm:"admin area"}
//val md = java.security.MessageDigest.getInstance("SHA-1");
//val enStr = new sun.misc.BASE64Encoder().encode("John:p4ssw0rd".getBytes)
//enStr == Sm9objpwNHNzdzByZA==
val realm = "admin area"

case class Auth(name:String){
 val authValue = config.getInt("spray.auth."+name)
 def hasReadingAccess = if( authValue ==1 || authValue == 3)true else false
 def hasWritingAccess = if( authValue ==2 || authValue == 3)true else false
}

def myUserPassAuthenticator(userPass: Option[UserPass]): Future[Option[Auth]] =
  Future {
    if (userPass.exists(up => config.getString("spray.users."+up.user) == up.pass || ( up.user == "John" && up.pass == "p4ssw0rd"))) Some(Auth(userPass.get.user))
    else None 
  }

val route =
  sealRoute {
    path("secured") {
      authenticate(BasicAuth(myUserPassAuthenticator _, realm = "secure site")) { userName =>
        complete(s"The user is '$userName'")
      }
    }
}

  startServer("localhost", port = 8080) {
    get {
      authenticate(BasicAuth(myUserPassAuthenticator _, realm = "admin area")) { user =>
      path("") {
        redirect("/hello", StatusCodes.Found)
      } ~
      path("hello") {
        complete {
          <html>
            <h1>Say hello to <em>spray</em> on <em>spray-can</em>!</h1>
            <p>(<a href="/stop?method=post">stop server</a>)</p>
          </html>
        }
      } ~
     path("orders") {
      authorize(user.hasReadingAccess) {
        complete {
           val out = JSONObject(Map("ret"->"op success","results" ->"all" )).toString()
           out 
        }
     }
     }
     }
    } ~
    (post | parameter('method ! "post")) {
      authenticate(BasicAuth(myUserPassAuthenticator _, realm = realm)) { user =>
      authorize(user.hasWritingAccess) {

      path("stop") {
        complete {
          system.scheduler.scheduleOnce(1.second)(system.shutdown())(system.dispatcher)
          "Shutting down in 1 second..."
        }
      } ~
      path("login") {
          entity(as[FormData]) { order =>
          complete {
           order 
          }
      }
      } ~
      path("orders") {
        entity(as[FormData]) { order =>
        complete {
           val out = JSONObject(Map("ret"->"op success","order" -> order)).toString()
           out
        } 
        } 
      }
     }
     }
    } ~ 
    pathPrefix("order" / IntNumber) { orderId =>
    authenticate(BasicAuth(myUserPassAuthenticator _, realm = realm)) { user =>
    path("") {
      get {
      authorize(user.hasReadingAccess) {
        complete {
          val out = JSONObject(Map("ret"->"op success","orderId" -> orderId,"user" -> user.hasReadingAccess)).toString()
              out
        }
      }
    } ~ 
    (put | parameter('method ! "put")) {
          // form extraction from multipart or www-url-encoded forms
          formFields('ename, 'age.as[Int]).as(Emp) { order =>
            complete {
              // complete with serialized Future result
              //(myDbActor ? Update(order)).mapTo[TransactionResult]
              val out = JSONObject(Map("orderId"->orderId,"ename"->order.ename,"age"->order.age)).toString()
              out
            }
          }
    } ~
    (delete | parameter('method ! "delete")) {
          // form extraction from multipart or www-url-encoded forms
            complete {
              // complete with serialized Future result
              //(myDbActor ? Update(order)).mapTo[TransactionResult]
              val out = JSONObject(Map("ret"->"op success","orderId" -> orderId)).toString()
              out
          }
  } 
    
}
}
}
}

}
