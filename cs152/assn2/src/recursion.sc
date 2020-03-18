/*
**  CS152 - Assignment 2
**  Mingyun Kim
*/
println("CS152 - Assignment 2")
println("Mingyun Kim")

def inc(n: Int): Int = n + 1
def dec(n: Int): Int = n - 1
def isZero(n: Int): Boolean = n == 0

/*
**  1. Re-define add
*/
def add(n: Int, m: Int): Int = if (isZero(m)) n else add(inc(n), dec(m))
println("1. add")
println("1 + 1 = " + add(1, 1))
println("3 + 3 = " + add(3, 3))
println("10 + 10 = " + add(10, 10))
println("42 + 0 = " + add(42, 0))
println("0 + 42 = " + add(0, 42))

/*
**  2. Re-define mul
*/
def mul(n: Int, m: Int): Int = {
	if (isZero(m))
		return 0
	def helper(result: Int, count: Int): Int =
		if (isZero(count)) result else helper(add(result, n), dec(count))
	helper(0, m)
}
println("2. mul")
println("1 x 1 = " + mul(1, 1))
println("3 x 3 = " + mul(3, 3))
println("10 x 10 = " + mul(10, 10))
println("42 x 0 = " + mul(42, 0))
println("0 x 42 = " + mul(0, 42))

/*
**  3. Re-define exp
*/
def exp(m: Int): Int = {
	def helper(result: Int, count: Int): Int =
		if (isZero(count)) result else helper(mul(result, 2), dec(count))
	helper(1, m)
}
println("3. exp")
println("2^0 = " + exp(0))
println("2^1 = " + exp(1))
println("2^3 = " + exp(3))
println("2^10 = " + exp(10))
println("2^31 - 1 = " + (exp(31) - 1))

/*
**  4. Define hyperExp
*/
def hyperExp(m: Int): Int = {
	if (isZero(m)) return 0
	def helper(result: Int, count: Int): Int =
		if (isZero(count)) result else helper(exp(result), dec(count))
	helper(1, dec(m))
}
println("4. hyperExp")
println("0 = " + hyperExp(0))
println("1 = " + hyperExp(1))
println("2 = " + hyperExp(2))
println("3 = " + hyperExp(3))
println("4 = " + hyperExp(4))
println("5 = " + hyperExp(5))

/*
**  9. Fibonacci
*/
def fib(n: Int): Int = {
	n match {
		case n if (n < 0) => 0
		case n if (n <= 1) => n
		case _ => fib(n - 1) + fib(n - 2)
	}
}

def tailFib(m: Int): Int = {
	if (m < 2)
		return m
	def helper(n1: Int, n2: Int, count: Int): Int =
		if (count == 0) n2 else helper(n2, n1 + n2, count - 1)
	helper(0, 1, m - 1)
}
println("9. Fibonacci")
for (i <- 0 to 10) println("fib(" + i + ") = " + fib(i))
for (i <- 0 to 10) println("tailFib(" + i + ") = " + tailFib(i))

/*
**  10. Choose
*/
def choose(n: Int, m: Int): Int = {
	m match {
		case m if (m == 0) => 1
		case m if (m == n) => 1
		case _ => choose(n - 1, m - 1) + choose(n - 1, m)
	}
}
println("10. Choose")
println("choose 1 from 2: " + choose(2, 1))
println("choose 2 from 5: " + choose(5, 2))
println("choose 3 from 8: " + choose(8, 5))
println("choose 10 from 17: " + choose(17, 10))
