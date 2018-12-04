package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

import play.api.libs.json.Json
import play.api.db._

import scala.util.Random

object Application extends Controller {
    
  def index = Action {
    var fatherTedQuote = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quotes (quote VARCHAR(1024))")

      val rs = stmt.executeQuery("SELECT quote FROM quotes")

      var quotes =
      new Iterator[String] {
        def hasNext = rs.next()
        def next() = rs.getString("quote")
      }.toStream


      fatherTedQuote += quotes(Random.nextInt(quotes.length))

    } finally {
      conn.close()
    }
    Ok(views.html.index(fatherTedQuote))
  }


  def putQuote(quoteStr: String) = Action { 
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quotes (quote VARCHAR(1024))")
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
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quotes (quote VARCHAR(1024))")
      
      val rs = stmt.executeQuery("SELECT quote FROM quotes")
      while (rs.next) {
        out += "Read from DB: " + rs.getString("quote") + "\n"
      }
    } finally {
      conn.close()
    }
    Ok(out)
  }
}
