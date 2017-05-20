import ShapelessLucene.{CricketPlayer, Record}
import org.apache.lucene.document.Document

/**
  * Created by tushark on 20/5/17.
  */
trait Data {

  val players: List[CricketPlayer] = List(CricketPlayer("Sachin", "Tendulkar", "Batsman", 36,17000, 125),
    CricketPlayer("Ravindrasinh", "Jadeja", "Batsman", 28, 1888, 44),
    CricketPlayer("Amit", "Mishra", "Bowler", 34, 43, 64),
    CricketPlayer("Gautam", "Gambhir", "Batsman", 36, 5238, 0),
    CricketPlayer("Rohit", "Sharma", "Batsman", 30, 5131, 8),
    CricketPlayer("Virat", "Kohli", "Batsman", 28, 7755, 4),
    CricketPlayer("Ajinkya", "Rahane", "Batsman", 28, 2237, 0),
    CricketPlayer("Umeshkumar", "Yadav", "Bowler", 29, 77, 88))

/*
RG Sharma 	264 	173 	33 	9 	152.60 		India 	v Sri Lanka 	Kolkata 	13 Nov 2014 	ODI # 3544
MJ Guptill 	237* 	163 	24 	11 	145.39 		New Zealand 	v West Indies 	Wellington 	21 Mar 2015 	ODI # 3643
V Sehwag 	219 	149 	25 	7 	146.97 		India 	v West Indies 	Indore 	8 Dec 2011 	ODI # 3223
CH Gayle 	215 	147 	10 	16 	146.25 		West Indies 	v Zimbabwe 	Canberra 	24 Feb 2015 	ODI # 3612
RG Sharma 	209 	158 	12 	16 	132.27 		India 	v Australia 	Bengaluru 	2 Nov 2013 	ODI # 3428
SR Tendulkar 	200* 	147 	25 	3 	136.05 		India 	v South Africa 	Gwalior 	24 Feb 2010 	ODI # 2962
CK Coventry 	194* 	156 	16 	7 	124.35 		Zimbabwe 	v Bangladesh 	Bulawayo 	16 Aug 2009 	ODI # 2873
Saeed Anwar 	194 	146 	22 	5 	132.87 		Pakistan 	v India 	Chennai 	21 May 1997 	ODI # 1209

Bowling :
WPUJC Vaas 	8.0 	3 	19 	8 	2.37 		Sri Lanka 	v Zimbabwe 	Colombo (SSC) 	8 Dec 2001 	ODI # 1776
Shahid Afridi 	9.0 	3 	12 	7 	1.33 		Pakistan 	v West Indies 	Providence 	14 Jul 2013 	ODI # 3389
GD McGrath 	7.0 	4 	15 	7 	2.14 		Australia 	v Namibia 	Potchefstroom 	27 Feb 2003 	ODI # 1970
AJ Bichel 	10.0 	0 	20 	7 	2.00 		Australia 	v England 	Port Elizabeth 	2 Mar 2003 	ODI # 1976

 */

  val records : List[Record] = List[Record](
    Record("Batting Inning", 375, 1, "Barbados", "01/02/1999", "Brian Lara"),
    Record("Batting Inning", 264, 1, "Kolkata", "13/09/2014", "Rohit Sharma"),
    Record("Batting Inning", 237, 0, "Wellington", "21/03/2015", "Martin Guptill"),
    Record("Batting Inning", 219, 1, "Indore", "08/12/2011", "Virender Sehwag"),
    Record("Batting Inning", 215, 1, "Canberra", "24/02/2015", "Chris Gayle"),
    Record("Batting Inning", 209, 1, "Bengaluru", "02/11/2013", "Rohit Sharma"),
    Record("Batting Inning", 200, 0, "Gwalior", "24/02/2010", "Sachin Tendulkar"),
    Record("Batting Inning", 194, 0, "Bulawayo", "16/08/2009", "CK Coventry"),
    Record("Batting Inning", 194, 1, "Bulawayo", "21/05/1997", "Saeed Anwar"),
    Record("Bowling Spell", 54, 10, "Kotla", "01/02/2004", "Anil Kumble"),
    Record("Bowling Spell", 19, 8, "Colombo", "08/12/2001", "Chaminda Vaas"),
    Record("Bowling Spell", 3, 7, "Providence", "14/07/2013", "Shahid Afridi"),
    Record("Bowling Spell", 15, 7, "Potchefstroom", "27/02/2003", "Glenn McGrath"))


  def indexPlayers() = {
    players.foreach( p => addToIndex(index(p)))
  }

  def indexRecords() = {
    records.foreach( p => addToIndex(index(p)))
  }

  def indexMatches() = {

  }

  def addToIndex(doc: Document) : Unit

  def index(obj: AnyRef) : Document

}
