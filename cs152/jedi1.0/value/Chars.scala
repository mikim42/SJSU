package value

import expression._

case class Chars(val v: String) extends Literal with Ordered[Chars] with Equals {
  def +(rhs: Chars): Chars = Chars(this.v + rhs.v)
  def ==(rhs: Chars) = if (this.v == rhs.v) true else false
  override def <(rhs: Chars) = if (this.v < rhs.v) true else false
  override def >(rhs: Chars): Boolean = if (this.v > rhs.v) true else false
  def substring(st: Integer, end: Integer) = Chars(v.substring(st.v, end.v))
  override def toString = v.toString
  def compare(rhs: Chars): Int = if (this.v < rhs.v) -1 else if (this.v == rhs.v) 0 else 1
  override def canEqual(rhs: Any) = rhs.isInstanceOf[Chars]
  override def equals(rhs:Any): Boolean =
    rhs match {
      case rhs: Chars => this.canEqual(rhs) && (this.v == rhs.v)
      case _ => false
    }
  override def hashCode = this.toString.##
}

object Chars {
  implicit def charsToReal(n: Chars): Real = Real(n.v.toDouble)
}