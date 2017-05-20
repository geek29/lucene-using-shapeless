import org.apache.lucene.document._
import shapeless._
import shapeless.labelled.FieldType

/**
  * Created by tushark on 19/5/17.
  */
object DocumentBuilder {

  trait FieldEncoder[A] {
    def encode(fieldName: String, value: A): Field
  }

  object FieldEncoder {
    def apply[A](implicit enc: FieldEncoder[A]): FieldEncoder[A] = enc
  }

  def createEncoder[A](func: (String, A) => Field): FieldEncoder[A] = new FieldEncoder[A] {
    def encode(fieldName: String, value: A): Field = func(fieldName, value)
  }

  implicit val stringEncoder: FieldEncoder[String] =
    createEncoder((fieldName, str) => new TextField(fieldName, str, Field.Store.NO))
  implicit val doubleEncoder: FieldEncoder[Double] =
    createEncoder((fieldName, num) => new DoublePoint(fieldName, num))
  implicit val intEncoder: FieldEncoder[Int] =
    createEncoder((fieldName, num) => new IntPoint(fieldName, num))

  implicit val booleanEncoder: FieldEncoder[Boolean] =
    createEncoder((fieldName, bool) => new StringField(fieldName, bool.toString, Field.Store.NO))

  trait DocumentEncoder[A] {
    def encode(value: A): Document
  }

  def createDocumentEncoder[A](fn: A => Document): DocumentEncoder[A] = new DocumentEncoder[A] {
    def encode(value: A): Document =
      fn(value)
  }

  implicit val hnilEncoder: DocumentEncoder[HNil] = createDocumentEncoder(hnil => new Document())

  implicit def hlistDocumentEncoder[K <: Symbol, H, T <: HList](
                                                                 implicit
                                                                 witness: Witness.Aux[K],
                                                                 hEncoder: Lazy[FieldEncoder[H]],
                                                                 tEncoder: DocumentEncoder[T]
                                                               ): DocumentEncoder[FieldType[K, H] :: T] = {

    val fieldName: String = witness.value.name

    createDocumentEncoder { hlist =>
      val head = hEncoder.value.encode(fieldName, hlist.head)
      val tail = tEncoder.encode(hlist.tail)
      tail.add(head)
      tail
    }
  }

  def writeLuceneDoc[H <: HList](value: H)(implicit enc: DocumentEncoder[H]): Document = enc.encode(value)

}
