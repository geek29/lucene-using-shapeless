/**
  * Created by tushark on 19/5/17.
  */

import java.io.File
import java.util.Scanner


object ShapelessLucene extends Data with Indexing {

  case class CricketPlayer(name: String, lastName: String, skill: String, age: Int, runs: Int, wickets: Int)
  case class TestMatch(homeTeam: String, awayTeam: String, date: String, winner: String, venue: String, line: String)
  case class Venue(name: String, place: String, city: String, country: String)
  case class Country(name: String)
  case class Record(`type`: String, runs: Int, wickets: Int, venue: String, date: String, player: String)

  //Print help containing sample queries :
  //https://lucene.apache.org/core/6_5_1/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description

  def queryREPL() = {
    val help = """
        |Some of sample queries you can try
        |To see all records scored at venue Barbados => venue: Barbados
        |To know Sachin's records or player profile => sachin OR player:sachin
        |List all bowling records => type:Bowling
        |List all bowlers => skill: Bats* or wicketkeepers skill: WicketKeeper
        |Rohit Sharma's all records => player:Rohit
        |Use quit to quit the program
      """.stripMargin
    println(s"$help")
    print(">")
    val scanner = new Scanner(System.in)
    var text = scanner.nextLine()
    while(text != "quit") {
      query(text)
      println("")
      print(">")
      text = scanner.nextLine()
    }
    closeIndexing()
  }

  def main(args: Array[String]): Unit = {
    createOrOpenIndex("./index")
    val dataCheckFile = new File("./index/data.lock")
    if(!dataCheckFile.exists()) {
      indexPlayers()
      indexRecords()
      indexMatches()
      commit()
      dataCheckFile.createNewFile()
    } else {
      println("Index already contains the sample data")
    }
    queryREPL()
  }

}
