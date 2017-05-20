import shapeless.labelled.FieldType
import shapeless.{::, HList, HNil, Lazy, Witness}

/**
  * Created by tushark on 20/5/17.
  */
object JSONBuilder {

  trait JSONFieldEncoder[A] {
    def encode(fieldName: String, value: A): String
  }

  object JSONFieldEncoder {
    def apply[A](implicit enc: JSONFieldEncoder[A]): JSONFieldEncoder[A] = enc
  }

  def createEncoder[A](func: (String,A) => String): JSONFieldEncoder[A] = new JSONFieldEncoder[A] {
    def encode(fieldName: String, value: A): String = func(fieldName, value)
  }

  implicit val stringEncoder: JSONFieldEncoder[String] =
    createEncoder((fieldName, str) => s""""$fieldName":"$str"""" )
  implicit val doubleEncoder: JSONFieldEncoder[Double] =
    createEncoder((fieldName,num) => s""""$fieldName":$num""")
  implicit val intEncoder: JSONFieldEncoder[Int] =
    createEncoder((fieldName,num) => s""""$fieldName":$num""")

  implicit val booleanEncoder: JSONFieldEncoder[Boolean] =
    createEncoder( (fieldName, bool) => s"""""$fieldName:$bool""")

  trait JSONObjectEncoder[A] {
    def encode(value: A): String
  }

  def createJSONObjectEncoder[A](fn: A => String): JSONObjectEncoder[A] = new JSONObjectEncoder[A] {
    def encode(value: A): String =
      fn(value)
  }

  implicit val hnilEncoder: JSONObjectEncoder[HNil] = createJSONObjectEncoder(hnil => "")

  implicit def hlistJSONObjectEncoder[K <: Symbol, H, T <: HList](
                                                                 implicit
                                                                 witness: Witness.Aux[K],
                                                                 hEncoder: Lazy[JSONFieldEncoder[H]],
                                                                 tEncoder: JSONObjectEncoder[T]
                                                               ): JSONObjectEncoder[FieldType[K, H] :: T] = {

    val fieldName: String = witness.value.name

    createJSONObjectEncoder { hlist =>
      val head = hEncoder.value.encode(fieldName, hlist.head)
      val tail = tEncoder.encode(hlist.tail)
      tail match {
        case "" => s"$head"
        case _ => s"$head,$tail"
      }
    }
  }

  def getJSON[H <: HList](value : H)(implicit enc: JSONObjectEncoder[H]) : String = s"{${enc.encode(value)}}"

}
