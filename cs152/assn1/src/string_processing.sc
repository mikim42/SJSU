{
	import scala.util.Random
	import java.util.Scanner

	val gen = new Random(System.currentTimeMillis())

	def isPal(s: String): Boolean = {
		for (i <- 1 to s.length)
			if (s(i - 1) != s(s.length - i)) return false
		true
	}
	println("1. isPal")
	println("rotator = " + isPal("rotator"))
	println("cat = " + isPal("cat"))
	println("civic = " + isPal("civic"))
	println("Toyota = " + isPal("Toyota"))
	println("$3.1441.3$ = " + isPal("$3.1441.3$"))

	def isPal2(s: String): Boolean = {
		var t = ""
		for (i <- 0 to s.length - 1) {
			if (s(i).isLetter) t += s(i).toLower
			else if (s(i).isDigit) t += s(i)
		}
		isPal(t)
	}
	println("2. isPal 2")
	println("A man, a plan, a canal, Panama! = " +
			isPal2("A man, a plan, a canal, Panama!"))

	def mkWord(n: Int = 5) = {
		var s = ""
		for (i <- 1 to n) s += (gen.nextInt(n) % 27 + 97).toChar
		s
	}
	println("3. Random String")
	println(mkWord())
	println(mkWord())
	println(mkWord())
	println(mkWord())
	println("20 = " + mkWord(20))

	def mkSentence(n: Int = 10) = {
		var s = ""
		for (i <- 0 to n - 1) {
			s += mkWord(gen.nextInt(12) % 10 + 1)
			if (i != n - 1) s += " "
			else s += "."
		}
		s.capitalize
	}
	println("4. Random Sentence")
	println(mkSentence())
	println(mkSentence())
	println(mkSentence())
	println(mkSentence())
	println("5 = " + mkSentence(5))

	def peano() = {
		println("Welcome to Peano 1.0")
		var input = new java.util.Scanner(System.in)
		print("-> ")
		var line = new Scanner(input.nextLine())
		while (line.equals("quit")) {
			print("-> ")
			line = new Scanner(input.nextLine())
			try {
				var a = line.nextDouble
				var o = line.next
				var b = line.nextDouble
				if (o == "+") println(a + b)
				else if (o == "*") println(a * b)
				else println("Missing operator!\nPeano syntax: expression ::= " +
				"operand~operator~operand\nwhere: operator ::= '+' | '*'")
			} catch {
				case _: Throwable => println("Illegal operand!\nPeano syntax: expression ::= " +
				"operand~operator~operand\nwhere: operand " +
				"::= any valid floating point number")
			}
		}
		println("bye")
	}
	println("5. Peano 1.0")
	peano()
}