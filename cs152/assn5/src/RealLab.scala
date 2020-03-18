/*
**  CS152 - Assignment 5
**  Mingyun Kim
*/
object RealLab extends App {

	class Real(val sv: Double) extends Ordered[Real] with Equals {
		def +(rhs: Real) = Real(this.sv + rhs.sv)

		def *(rhs: Real) = Real(this.sv * rhs.sv)

		def -(rhs: Real) = Real(this.sv - rhs.sv)

		def /(rhs: Real) = Real(this.sv / rhs.sv)

		def ==(rhs: Real) = this.sv == rhs.sv

		override def <(rhs: Real) = this.sv < rhs.sv

		override def >(rhs: Real) = this.sv > rhs.sv

		override def canEqual(rhs: Any) = rhs.isInstanceOf[Real]

		override def compare(rhs: Real) = {
			var tmp = this.sv - rhs.sv
			if (tmp == 0) 0
			else if (tmp > 0) 1
			else -1
		}

		override def toString = sv.toString

		override def hashCode = this.toString.##

		override def equals(rhs: Any) = {
			rhs match {
				case rhs: Real => this.canEqual(rhs) &&
				this.## == rhs.## && this.sv == rhs.sv
				case _ => false
			}
		}
	}

	object Real {
		def apply(x: Double) = new Real(x)
	}

	var r1 = Real(3.14)
	var r2 = Real(2.71)
	println("r1 * r2 = " + (r1 * r2))
	println("r1 == r2 = " + (r1 == r2))
	println("r1 < r2 = " + (r1 < r2))
}
