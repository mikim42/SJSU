package value

import expression._

case class Real(val v: Double) extends Literal with Ordered[Real] with Equals {
  def +(rhs: Real) = Real(this.v + rhs.v)
  def *(rhs: Real) = Real(this.v * rhs.v)
  def -(rhs: Real) = Real(this.v - rhs.v)
  def /(rhs: Real) = Real(this.v / rhs.v)
  def unary_- = Real(-this.v)
  override def toString = v.toString
  def compare(rhs: Real): Int = if (this.v < rhs.v) -1 else if (this.v == rhs.v) 0 else 1
  override def canEqual(rhs: Any) = rhs.isInstanceOf[Real]
  override def equals(rhs:Any): Boolean =
    rhs match {
      case rhs: Real => this.canEqual(rhs) && (this.v == rhs.v)
      case _ => false
    }
  override def hashCode = this.toString.##
}

object Real {
  implicit def realToReal(n: Real): Real = Real(n.v.toDouble)
}