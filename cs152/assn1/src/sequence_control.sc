/*
**  Problem 1. Number Kingdoms
*/
{
	def kingdoms(n: Int): Int = {
		if (n <= 10) 3
		else if (n % 100 == 0) 2
		else if (n % 2 == 0) 1
		else 4
	}
	println("1. Number Kingdoms")
	println("5   => " + kingdoms(5))
	println("10  => " + kingdoms(10))
	println("50  => " + kingdoms(50))
	println("100 => " + kingdoms(100))
	println("101 => " + kingdoms(101))
	println("102 => " + kingdoms(102))
	println("333 => " + kingdoms(333))
}

/*
**  Problem 2. Number Orders
*/
{
	def orders(n: Int): Int = {
		if (n < 0) 0 else (if (n % 3 == 0) 1 else 2) * (if (n == 50) 3 else 4) +
		(if (n % 7 == 0) 5 else 6)
	}
	println("2. Number Orders")
	println("-10 => " + orders(-10))
	println("-4  => " + orders(-4))
	println("-1  => " + orders(-1))
	println("0   => " + orders(0))
	println("1   => " + orders(1))
	println("3   => " + orders(3))
	println("5   => " + orders(5))
	println("7   => " + orders(7))
	println("21  => " + orders(21))
	println("29  => " + orders(29))
	println("50  => " + orders(50))
	println("172 => " + orders(172))
	println("328 => " + orders(328))
}

/*
**  Problem 3. Number Species
*/
{
	/*
	**  2 if statements need to be combined by && boolean expression
	*/
	def species(n: Int): Int = {
		// if (0 < n) if (n % 2 == 0) 1 else 2
		if (0 < n && n % 2 == 0) 1 else 2
	}
	println("3. Number Species")
	println("-10 => " + species(-10))
	println("-4  => " + species(-4))
	println("-1  => " + species(-1))
	println("0   => " + species(0))
	println("1   => " + species(1))
	println("5   => " + species(5))
	println("8   => " + species(8))
	println("9   => " + species(9))
	println("20  => " + species(20))
	println("111 => " + species(111))
	println("328 => " + species(328))
}

/*
**  Problem 4. Elbonian Tax Calculator
*/
{
	@throws(classOf[Exception])
	def tax(n: Int): Double = {
		n match {
				case n if (n < 0) => throw new Exception("invalid income")
			case n if (n < 20000) => 0
				case n if (n < 30000) => n * 0.05
				case n if (n < 40000) => n * 0.11
				case n if (n < 60000) => n * 0.23
				case n if (n < 100000) => n * 0.32
				case n if (n >= 100000) => n * 0.5
		}
	}
	println("4. Elbonian Tax Calculator")
	println("12300   => " + tax(12300))
	println("29000   => " + tax(29000))
	println("53000   => " + tax(53000))
	println("78000   => " + tax(78000))
	println("125000  => " + tax(125000))
	println("1000000 => " + tax(1000000))
	try {
		println("-100000 => " + tax(-100000))
	} catch {
		case e: Exception => println("-100000 => " + e)
	}
}

/*
**  Problem 5. Draw Rectangle
*/
{
	def drawRectangle(x: Int, y: Int) = {
		for (i <- 1 to y) {
			for (j <- 1 to x)
				print("*")
			println()
		}
	}
	println("5. Draw Rectangle")
	println("(1, 1)")
	drawRectangle(1, 1)
	println("(3, 3)")
	drawRectangle(3, 3)
	println("(5, 5)")
	drawRectangle(5, 5)
	println("(5, 2)")
	drawRectangle(5, 2)
}

/*
**  Problem 6. Print Sum
*/
{
	def printSums(n: Int, m: Int)  = {
		for (i <- 0 to n - 1; j <- 0 to m - 1)
			println(i + " + " + j + " = " + (i + j))
	}
	println("6. Print Sum")
	println("(3, 4)")
	printSums(3, 4)
}

/*
**  Problem 7. Life without breaks
*/
{
	/*
	**  I spotted that there are syntax format differences but they are
	**  most likely similar in how it functions
	*/
	import scala.util._
	import util.control.Breaks._

	/*
	**  I have commented out declaration since it gives errors on IntelliJ IDE
	*/
	//object BlackJack1 extends App {
		val gen = new Random(System.currentTimeMillis())
		val cards = new Array[Int](52)

		for(i <- 0 until 52) cards(i) =
			if (gen.nextBoolean()) gen.nextInt(11) else -1

		println("7. Life without breaks")
		// Version 1
		var total = 0
		breakable {
			for (i <- cards) {
				breakable {
					if (i < 0) break else total += i
				}
				if (total >= 21) break
			}
		}
		println("Version 1: total = " + total)

		// Version 2
		total = 0
		try {
			for (i <- cards) {
				if (total >= 21) throw new Exception
				try {
					if (i < 0) throw new Exception else total += i
				} catch {
					case _: Throwable =>
				}
			}
		} catch {
			case _: Throwable =>
		}
		println("Version 2: total = " + total)

		// Version 3
		total = 0
		for (i <- cards if total < 21) {
			if (0 < i) total += i
		}
		println("Version 3: total = " + total)
	//}
}

/*
**  Problem 8. Realm
*/
{
	@throws(classOf[Exception])
	def realm1(n: Int): Int = {
		if (n % 2 == 1) 1 else throw new Exception
	}
	@throws(classOf[Exception])
	def realm2(n: Int): Int = {
		if (n % 2 == 0 && n % 3 != 0) 2 else throw new Exception
	}
	@throws(classOf[Exception])
	def realm3(n: Int): Int = {
		if (n % 6 == 0 || n % 7 == 0) 3 else throw new Exception
	}
	@throws(classOf[Exception])
	def realm(n: Int): Int = {
		if (n <= 0) 0 else try realm1(n) catch {
			case _: Throwable => try realm2(n) catch {
				case _: Throwable => try realm3(n) catch {
					case _: Throwable => 0
				}
			}
		}
	}
	println("8. Realm")
	println("-10 => " + realm(-10))
	println("-4  => " + realm(-4))
	println("-1  => " + realm(-1))
	println("0   => " + realm(0))
	println("1   => " + realm(1))
	println("5   => " + realm(5))
	println("6   => " + realm(6))
	println("7   => " + realm(7))
	println("8   => " + realm(8))
	println("9   => " + realm(9))
	println("12  => " + realm(12))
	println("14  => " + realm(14))
	println("20  => " + realm(20))
	println("111 => " + realm(111))
	println("328 => " + realm(328))
}

/*
**  Problem 9. Monadic Binding
*/
{
	def log(x: Double) = if (x <= 0) None else Some(math.log(x))
	def sqrt(x: Double) = if (x < 0) None else Some(math.sqrt(x))

	def sqrtLog(x: Double) = {
		sqrt(x)
		x match {
			case x if (log(x) == None) => None
			case _ => sqrt(log(x).get)
		}
	}
	println("9. Monadic Binding")
	println("-10 => " + sqrtLog(-10))
	println("-4  => " + sqrtLog(-4))
	println("-1  => " + sqrtLog(-1))
	println("0   => " + sqrtLog(0))
	println("1   => " + sqrtLog(1))
	println("5   => " + sqrtLog(5))
	println("6   => " + sqrtLog(6))
	println("7   => " + sqrtLog(7))
	println("8   => " + sqrtLog(8))
	println("9   => " + sqrtLog(9))
	println("12  => " + sqrtLog(12))
	println("14  => " + sqrtLog(14))
	println("20  => " + sqrtLog(20))
	println("111 => " + sqrtLog(111))
	println("328 => " + sqrtLog(328))
}
