package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

import play.api.libs.json.Json
import play.api.db._

import scala.util.Random

object Application extends Controller {

  val fatherTedQuotes = List(
	"There was... a spider in the bath last night.",
	"Oh worse than Hitler. You wouldn't find Hitler playing jungle music at three o'clock in the morning!",
	"It's Irelands biggest lingerie section I understand. I read that... somewhere.",
	"There’s nothing stupid about football! And there’s nothing at all stupid about the annual All-Priests Five-a-Side over 75s Indoor Football Challenge Match, against Rugged Island!"
  )
    
  def index = Action {
    Ok(views.html.index(fatherTedQuotes(Random.nextInt(fatherTedQuotes.length))))
  }
  
  
  def putQuote(quoteStr: String) = Action { 
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quotes (quote VARCHAR(1024)")
      stmt.executeUpdate(s"INSERT INTO quotes VALUES ('$quoteStr')")

    } finally {
      conn.close()
    }
    Ok(quoteStr)
  }
  
  def db = Action {
    var out = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())")

      val rs = stmt.executeQuery("SELECT tick FROM ticks")

      while (rs.next) {
        out += "Read from DB: " + rs.getTimestamp("tick") + "\n"
      }
    } finally {
      conn.close()
    }
    Ok(out)
  }
}
