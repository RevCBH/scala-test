import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client._
import org.apache.http.auth._
import org.apache.http.entity._
import org.codehaus.jettison.json._
import scala.tools.nsc.io._

abstract class BitcoinCommand {
  val command: String

  def toJSON = {
    val json = new JSONObject()
    json.`put`("method", command)
  }
}

case class GetInfo extends BitcoinCommand {
  val command = "getinfo"
}

object Test extends Application {
  val config = new JSONObject(File("./config.json").slurp)
  val rpcuser = (config get "rpcuser").toString
  val rpcpassword = (config get "rpcpassword").toString
  val client = new DefaultHttpClient()
  client.getCredentialsProvider().setCredentials(
    new AuthScope("localhost", 8332),
    new UsernamePasswordCredentials(rpcuser, rpcpassword))

  val post = new HttpPost("http://localhost:8332")
  post.setEntity(new StringEntity(GetInfo().toJSON.toString()))

  println("executing: " + post.getRequestLine())
  println(GetInfo().toJSON)

  val responseHandler = new BasicResponseHandler()
  val response = client.execute(post, responseHandler)
  println("\n---------------- response: ")
  println(response)

}

