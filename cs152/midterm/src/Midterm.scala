
/*
**  CS152 - Midterm
**  3. List Processing
**  Mingyun Kim
*/
object Midterm extends App {

	class Flight(val fno: Int, val eta: Int, val ata: Int, cc: Boolean = false)
	extends Ordered[Flight] with Equals {
		def +(rhs: Flight) = Flight(this.fno, this.eta + rhs.eta, this.ata + rhs.ata, false)

		override def canEqual(rhs: Any) = rhs.isInstanceOf[Flight]

		override def compare(rhs: Flight) = {
			var tmp = this.fno - rhs.fno
			if (tmp == 0) 0
			else if (tmp > 0) 1
			else -1
		}

		override def toString = {
			if (!this.cc) {
				"#" + this.fno.toString +
				" ETA:" + "%02d".format(this.eta / 60) + ":" + "%02d".format(this.eta % 60) +
				" ATA:" + "%02d".format(this.ata / 60) + ":" + "%02d".format(this.ata % 60)
			} else "#" + this.fno.toString + " has been cancelled"
		}

		override def hashCode = this.toString.##

		override def equals(rhs: Any) = {
			rhs match {
				case rhs: Flight => this.canEqual(rhs) &&
				this.## == rhs.## && this.fno == rhs.fno
				case _ => false
			}
		}

		val delay = ata - eta

		def isCC = cc
	}

	object Flight {
		def apply(fno: Int, eta: Int, ata: Int, cc: Boolean = false) = {
			new Flight(fno, eta, ata, cc)
		}
	}

	val flightsMarch3 = List(Flight(123, 0, 30), Flight(234, 60, 65),
		Flight(345, 200, 200, true), Flight(456, 420, 510))
	for (e <- flightsMarch3)
		println(e)

	def avgDelay(list: List[Flight]): Double = {
		list.reduce(_ + _).delay.toDouble / list.filter(!_.isCC).size.toDouble
	}

	println(avgDelay(flightsMarch3))
}
