import ShapelessLucene.{CricketPlayer, Record, TestMatch, Venue}
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, StoredField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.{ParseException, QueryParser}
import org.apache.lucene.search.{IndexSearcher, Query, ScoreDoc}
import org.apache.lucene.store.SimpleFSDirectory
import shapeless.LabelledGeneric

/**
  * Created by tushark on 20/5/17.
  */
trait Indexing {

  var indexWriter: IndexWriter = _

  def createOrOpenIndex(path: String) = {
    val config = new IndexWriterConfig(new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET))
    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)
    indexWriter = new IndexWriter(new SimpleFSDirectory(new java.io.File(path).toPath), config)
  }

  def addToIndex(doc: Document): Unit = {
    indexWriter.addDocument(doc)
  }

  val lgCP = LabelledGeneric[CricketPlayer]
  val lgTM = LabelledGeneric[TestMatch]
  val lgV = LabelledGeneric[Venue]
  val lgR = LabelledGeneric[Record]

  def index(obj: AnyRef): Document = {
    val (doc, json, payloadType) = obj match {
      case k: CricketPlayer =>
        val hListRepr = lgCP.to(k)
        (DocumentBuilder.writeLuceneDoc(hListRepr), JSONBuilder.getJSON(hListRepr), "CricketPlayer")
      case k: TestMatch =>
        val hListRepr = lgTM.to(k)
        (DocumentBuilder.writeLuceneDoc(hListRepr), JSONBuilder.getJSON(hListRepr), "TestMatch")
      case k: Venue =>
        val hListRepr = lgV.to(k)
        (DocumentBuilder.writeLuceneDoc(hListRepr), JSONBuilder.getJSON(hListRepr), "Venue")
      case k: Record =>
        val hListRepr = lgR.to(k)
        (DocumentBuilder.writeLuceneDoc(hListRepr), JSONBuilder.getJSON(hListRepr), "Record")
    }
    doc.add(new StoredField("_json", json))
    doc.add(new StoredField("_type", payloadType))
    doc
  }

  def query(text: String) = {
    val reader = DirectoryReader.open(indexWriter.getDirectory)
    val searcher = new IndexSearcher(reader)
    try {
      val parser: QueryParser = new QueryParser("name", new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET))
      val query: Query = parser.parse(text)
      val hits: Array[ScoreDoc] = searcher.search(query, 1000).scoreDocs
      println(s"Search results : ${hits.length} docs found")
      // Iterate through the results:
      if (hits.length > 0) {
        for (i <- hits.indices) {
          val hitDoc: Document = searcher.doc(hits(i).doc)
          val json = hitDoc.get("_json")
          val _type = hitDoc.get("_type")
          println(s"DOC#$i: type: ${_type} : $json")
        }
      }
    } catch {
      case e: ParseException =>
        println(s"Query Parsing Failed ${e.getMessage}")
    } finally {
      reader.close()
    }
  }

  def commit() = {
    indexWriter.commit()
  }

  def closeIndexing() = {
    indexWriter.close()
  }

}
