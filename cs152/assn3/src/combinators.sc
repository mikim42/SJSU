/*
**  CS152 - Assignment 3
**  Mingyun Kim
*/
println("CS152 - Assignment 3")
println("Mingyun Kim")

def inc(x: Double): Double = x + 1
def double(x: Double): Double = 2 * x
println("f(x) = x + 1, g(x) = 2 * x")

/*
**  1. Composition
*/
def compose[T](f: T => T, g: T => T): T => T = (x: T) => f(g(x))

println("1. Composition")
var f = compose(inc _, double _)
println("f(g(5)) = " + f(5))
var f = compose(double _, inc _)
println("g(f(5)) = " + f(5))

/*
**  2. Combinator
*/

def selfIter[T](f: T => T, n: Int): T => T = {
	def recur(x: T, count: Int): T = {
		if (count == 0) x
		else recur(f(x), count - 1)
	}
	recur( _, n)
}

println("2. Combinator")
println("self iterator: f(5), 7")
var f = selfIter(inc, 7)
println(f(5))
println("self iterator: g(5), 7")
var f = selfIter(double, 7)
println(f(5))

/*
**  3. countPass
*/
def divisibleThree(n: Int): Boolean = if (n % 3 == 0) true else false

def countPass[T](arr: Array[T], test: T => Boolean): Int = {
	var result = 0
	for (elem <- arr) if (test(elem)) result += 1
	result
}

println("3. countPass")
println("Count if a number is divisible by 3")
println("Numbers: 1 - 9, " + countPass(Array(1, 2, 3, 4, 5, 6, 7, 8, 9), divisibleThree))

/*
**  4. Recur Combinator
*/

def recur(baseVal: Int, combiner: (Int, Int) => Int): Int => Int = {
	def helper(n: Int): Int = if (n == 0) baseVal else combiner(n, helper(n - 1))
	helper _
}

println("4. Recur Combinator")
val factorial = recur(1, _ * _)
println("Factorial 10: " + factorial(10))
println("Factorial 5 : " + factorial(5))

/*
**  5. deOptionize
*/

def parseDigits(digits: String): Option[Int] =
	if (digits.matches("[0-9]*")) Some(digits.toInt) else None

def deOptionize[T1, T2](f: T1 => Option[T2]): T1 => T2 = {
	def helper(x: T1) = {
		f(x) match {
			case None => throw new Exception("Error")
			case Some(y) => y
		}
	}
	helper _
}

println("5. deOptionize")
val f = deOptionize[String, Int](parseDigits)
println("42: " + f("42"))
try {
	println("hello world:" + f("hello world"))
} catch {
	case _: Throwable => println("Error found")
}

/*
**  6. makeIter
*/

def makeIter[T](initVal: T, combiner: (T, Int) => T) = {
	def res(n: Int) = {
		var result = initVal
		for (count <- 1 to n) result = combiner(result, count)
		result
	}
	res _
}

def add(x: Int, y: Int) = x + y
val tri = makeIter[Int](0, add _)
println(tri(5))

val fact = makeIter[Int](1, (x: Int, y: Int) => x * y)
println(fact(5))

def blah(s: String, r: Int) = {
	var result = s
}
val foop = makeIter[String]("hello", (x: String, y: Int) => x + " " + x)
println(foop(3))

/*
**  7. unitTest
*/

def cube(n: Int) = n * n * n

def unitTest[T, S](f: T => S, arr: Array[(T, S)]) = {
	var result = 0
	for (elem <- arr) if (f(elem._1) != elem._2) result += 1
	result
}

println("7. unitTest")
println(unitTest(cube, Array((1, 1), (2, 8), (3, 9), (4, 64), (5, 124))))

/*
**  8. DDS
*/

println("8-1. Control Loop")
def controlLoop[S](
	state: S,
	cycle: Int,
	halt: (S, Int) => Boolean,
	update: (S, Int) => S): S =
		if (halt(state, cycle)) state
		else controlLoop(update(state, cycle), cycle + 1, halt, update)

/*
**  8-2. Population Growth
*/

def population(initPop: Int): Int = {
	def isExceeded(v: Int, t: Int) = 100000 <= v
	def reproduction(v: Int, t: Int) = 2 * v

	controlLoop(initPop, 0, isExceeded, reproduction)
}

println("Init Population 500: " + population(500))
println("Init Population 3333: " + population(3333))

/*
**  8-3. Finding Roots of Functions
*/

def solve(f: Double => Double): Double = {
	val delta = 1e-12
	def df(x: Double) = (f(x + delta) - f(x))/delta
	def goodEnuf(guess: Double, time: Int) = math.abs(f(guess)) <= delta
	def improve(guess: Double, time: Int) = guess - f(guess)/df(guess)
	controlLoop(1.0, 0, goodEnuf, improve)
}

/*
**  8-4. Square Root
*/

def squareRoot(x: Double) = solve((y: Double) => y * y - x)

println("8-4. Square Root")
println("square root of 16 : " + squareRoot(16))
println("square root of 36 : " + squareRoot(36))

/*
**  8-5. Cube Root
*/

def cubeRoot(x: Double) = solve((y: Double) => y * y * y - x)

println("8-5. Cube Root")
println("cube root of 27 : " + cubeRoot(27))
println("cube root of 64 : " + cubeRoot(64))

/*
**  8-6. N-th Root
*/

def nthRoot(x: Double, n: Int) = {
	def power(x1: Double, n1: Int) = {
		var m = 1.0
		for (i <- 1 to n1) m *= x1
		m
	}
	solve((y: Double) => power(y, n) - x)
}

println("8-6. n-th Root")
println("n-th root of 4, 2: " + nthRoot(4, 2))
println("n-th root of 27, 3: " + nthRoot(27, 3))
println("n-th root of 256, 4: " + nthRoot(256, 4))

/*
**  8-7. Compound Interest
*/

def getValue(periods: Int) = {
	def isMature(v: Double, time: Int) = periods <= time
	def compound(v: Double, time: Int) = v + (1.0 / periods) * v

	controlLoop(1.0, 0, isMature, compound)
}

println("8-7. Compound Interest")
getValue(12)
getValue(52)
getValue(365)
getValue(365 * 24 * 60)
