package value

import expression._

case class Integer(val v: Int) extends Literal with Ordered[Integer] with Equals {
  def +(rhs: Integer) = Integer(this.v + rhs.v)
  def *(rhs: Integer) = Integer(this.v * rhs.v)
  def -(rhs: Integer) = Integer(this.v - rhs.v)
  def /(rhs: Integer) = Integer(this.v / rhs.v)
  def unary_- = Integer(-this.v)
  override def toString = v.toString
  def compare(rhs: Integer): Int = if (this.v < rhs.v) -1 else if (this.v == rhs.v) 0 else 1
  override def canEqual(rhs: Any) = rhs.isInstanceOf[Integer]
  override def equals(rhs:Any): Boolean =
    rhs match {
      case rhs: Integer => this.canEqual(rhs) && (this.v == rhs.v)
      case _ => false
    }
  override def hashCode = this.toString.##
}

object Integer {
  implicit def intToReal(n: Integer): Real = Real(n.v.toDouble)
}