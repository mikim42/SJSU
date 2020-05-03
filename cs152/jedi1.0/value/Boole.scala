package value

import expression._

case class Boole(val v: Boolean) extends Literal with Ordered[Boole] with Equals {
  def &&(rhs: Boole) = Boole(this.v && rhs.v)
  def ||(rhs: Boole) = Boole(this.v || rhs.v)
  def unary_! = Boole(!this.v)
  def compare(rhs: Boole): Int = if (this.v && !rhs.v) 1 else if (this.v == rhs.v) 0 else -1
  override def toString = v.toString
  override def canEqual(rhs: Any) = rhs.isInstanceOf[Boole]
  override def equals(rhs:Any): Boolean =
    rhs match {
      case rhs: Boole => this.canEqual(rhs) && (this.v == rhs.v)
      case _ => false
    }
  override def hashCode = this.toString.##
}

object Boole {
  implicit def intToReal(n: Integer): Real = Real(n.v.toDouble)
}