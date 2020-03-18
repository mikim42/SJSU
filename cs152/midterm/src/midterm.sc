
/*
**  CS152 - Midterm
**  Mingyun Kim
*/

/*
**  1. Count Roots
*/

def countRoots[T](f: T => Double, input: List[T]): Int = {
	var result = 0
	for (elem <- input if f(elem) == 0.0) result += 1
	result
}

def poly(x: Double) = (x + 1) * (x - 1)
println(countRoots(poly _, List(-1.0, 0.0, 1.0, 2.0)))

/*
**  2. Combinator
*/

/*
**  def double(n: Int): Int = if (n == 0) 0 else 2 + double(n - 1)
**  def exp(n: Int): Int = if(n == 0) 1 else double(exp(n - 1))
**  def hyperExp(n: Int): Int = if(n == 0) 1 else exp(hyperExp(n - 1))
**  def hyper2Exp(n: Int): Int = if(n == 0) 1 else exp(hyperExp(n - 1))
*/

def recur(baseVal: Int, combiner: (Int) => Int): Int => Int = {
	def helper(n: Int): Int = if (n == 0) baseVal else combiner(helper(n - 1))
	helper _
}

val double = recur(0, 2 + _)
println(double(32))
val exp = recur(1, double(_))
println(exp(8))
val hyperExp = recur(1, exp(_))
println(hyperExp(3))
val hyper2Exp = recur(1, hyperExp(_))
println(hyper2Exp(2))

