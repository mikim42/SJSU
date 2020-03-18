/*
**  CS152 - Assignment 5
**  Mingyun Kim
*/
object RecognizersLab extends App {

	trait Recognizers {
		def matches(s: String): String => Boolean = {
			def helper(s1: String) = s1.trim.equalsIgnoreCase(s)
			helper _
		}

		def opt(r: String => Boolean): String => Boolean = {
			def helper(s: String) = r(s) || s == ""
			helper _
		}

		def pipe(r1: String => Boolean, r2: String => Boolean): String => Boolean = {
			def helper(s: String) = r1(s) || r2(s)
			helper _
		}

		def follows(r1: String => Boolean, r2: String => Boolean): String => Boolean = {
			def helper(s: String): Boolean = {
				var result = false
				for (i <- 0 to s.length if !result)
					result = r1(s.take(i)) && r2(s.drop(i))
				result
			}
			helper _
		}

		def rep(r: String => Boolean): String => Boolean = {
			def r1(s: String): Boolean = {
				var result = false
				if (s == "") result = true
				else {
					for (i <- 0 to s.size if !result)
						result = r(s.substring(0, i)) && r1(s.substring(i))
				}
				result
			}
			r1 _
		}
	}

	class Test extends Recognizers {
		def exp1 = pipe(follows(matches("00"), matches("11")), matches("111"))

		def exp2 = follows(follows(rep(matches("00")), matches("111")), opt(matches("00")))

		def exp3 = pipe(follows(matches("00"), follows(rep(matches("111")), opt(matches("00")))), matches("11111"))
	}

	object Test {
		def apply() = new Test
	}

	println(Test().exp1("0011"))
	println(Test().exp1("111"))
	println(Test().exp1("000011"))
	println()
	println(Test().exp2("0000111"))
	println(Test().exp2("00000011100"))
	println(Test().exp2("00011100"))
	println(Test().exp2("11100"))
	println()
	println(Test().exp3("00111111"))
	println(Test().exp3("0011100"))
	println(Test().exp3("11111"))
	println(Test().exp3("110000011"))
}
