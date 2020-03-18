/*
**  CS152 - Assignment 4
**  Mingyun Kim
*/

import scala.math. _

/*
**  1. Sum of Cube
*/

def isOdd(n : Int): Boolean = n % 2 == 1
def cube(n: Int): Int = n * n * n

def sumOddCubeIter(nums: List[Int]): Int = {
	var sum = 0
	for (elem <- nums if isOdd(elem)) sum += cube(elem);
	sum
}

println("1-1. Sum of Cube - Iterative")
sumOddCubeIter(List())
sumOddCubeIter(List(1, 2, 3, 4, 5))
sumOddCubeIter(List(2, 2, 4, 4, 6))
sumOddCubeIter(List(1, 3, 5, 7, 9))

def sumOddCubeRec(nums: List[Int]): Int = {
	if (nums == Nil) 0
	else if (isOdd(nums.head)) cube(nums.head) + sumOddCubeRec(nums.tail)
	else sumOddCubeRec(nums.tail)
}

println("1-2. Sum of Cube - Recursive")
sumOddCubeRec(List())
sumOddCubeRec(List(1, 2, 3, 4, 5))
sumOddCubeRec(List(2, 2, 4, 4, 6))
sumOddCubeRec(List(1, 3, 5, 7, 9))

def sumOddCubeTail(nums: List[Int]) = {
	def helper(result: Int, list: List[Int]): Int = {
		if (list == Nil) result
		else if (isOdd(list.head)) helper(result + cube(list.head), list.tail)
		else helper(result, list.tail)
	}
	helper(0, nums);
}

println("1-3. Sum of Cube - Tail")
sumOddCubeTail(List())
sumOddCubeTail(List(1, 2, 3, 4, 5))
sumOddCubeTail(List(2, 2, 4, 4, 6))
sumOddCubeTail(List(1, 3, 5, 7, 9))

def sumOddCubePipe(nums: List[Int]): Int = {
	if (nums.filter(isOdd).map(cube) == Nil) 0
	else nums.filter(isOdd).map(cube).reduce((x: Int, y: Int) => x + y)
}

println("1-4. Sum of Cube - Pipeline")
sumOddCubePipe(List())
sumOddCubePipe(List(1, 2, 3, 4, 5))
sumOddCubePipe(List(2, 2, 4, 4, 6))
sumOddCubePipe(List(1, 3, 5, 7, 9))

/*
**  2. Sum Of Sums
*/

def sumList(list: List[Int]): Int =
	if (list == Nil) 0 else list.reduce((x: Int, y: Int) => x + y)

def sumOfSumsIter(list: List[List[Int]]): Int = {
	var sum = 0
	for(elem <- list) sum += sumList(elem);
	sum
}

println("2-1. Sum of Sums - Iterative")
sumOfSumsIter(List(List(1, 2), List(3, 4), List(5, 6, 7)))

def sumOfSumsRec(list: List[List[Int]]): Int = {
	if (list == Nil) 0
	else sumList(list.head) + sumOfSumsRec(list.tail)
}

println("2-2. Sum of Sums - Recursive")
sumOfSumsRec(List(List(1, 2), List(3, 4), List(5, 6, 7)))

def sumOfSumsTail(list: List[List[Int]]): Int = {
	def helper(result: Int, l: List[List[Int]]): Int = {
		if (l == Nil) result
		else helper(result + sumList(l.head), l.tail)
	}
	helper(0, list)
}

println("2-3. Sum of Sums - Tail")
sumOfSumsTail(List(List(1, 2), List(3, 4), List(5, 6, 7)))

def sumOfSumsPipe(list: List[List[Int]]): Int =
	if (list == Nil) 0 else list.map(sumList).reduce(_ + _)

println("2-4. Sum of Sums - Tail")
sumOfSumsTail(List(List(1, 2), List(3, 4), List(5, 6, 7)))

/*
**  6. Number of Elements
*/

def assertCountIter[T](list: List[T], pred: T => Boolean): Int = {
	var result = 0
	for (elem <- list) if (pred(elem)) result += 1
	result
}

println("6-1. Assert Count - Iterative")
println(assertCountIter(List(1, 2, 3, 4, 5), isOdd))

def assertCountRec[T](list: List[T], pred: T => Boolean): Int = {
	if (list == Nil) 0
	else if (pred(list.head)) 1 + assertCountRec(list.tail, pred)
	else assertCountRec(list.tail, pred)
}

println("6-2. Assert Count - Recursive")
println(assertCountRec(List(1, 2, 3, 4, 5), isOdd))

def assertCountTail[T](list: List[T], pred: T => Boolean): Int = {
	def helper(result: Int, l: List[T]): Int = {
		if (l == Nil) result
		else if (pred(list.head)) helper(result + 1, l.tail)
		else helper(result, l.tail)
	}
	helper(0, list)
}

println("6-3. Assert Count - Tail")
println(assertCountIter(List(1, 2, 3, 4, 5), isOdd))

def assertCountPipe[T](list: List[T], pred: T => Boolean): Int =
	list.filter(pred).size

println("6-4. Assert Count - Pipe")
println(assertCountIter(List(1, 2, 3, 4, 5), isOdd))

/*
**  7. Assert List
*/

def assertListIter[T](list: List[T], pred: T => Boolean): Boolean = {
	var result = true
	for (elem <- list if result) result = pred(elem)
	result
}

println("7-1. Assert List - Iterative")
println(assertListIter(List(1, 3, 5, 7), isOdd))
println(assertListIter(List(1, 3, 5, 6), isOdd))
println(assertListIter(List(1, 2, 5, 7), isOdd))

def assertListRec[T](list: List[T], pred: T => Boolean): Boolean = {
	if (list == Nil) true
	else pred(list.head) && assertListRec(list.tail, pred)
}

println("7-2. Assert List - Iterative")
println(assertListRec(List(1, 3, 5, 7), isOdd))
println(assertListRec(List(1, 3, 5, 6), isOdd))
println(assertListRec(List(1, 2, 5, 7), isOdd))

def assertListTail[T](list: List[T], pred: T => Boolean): Boolean = {
	def helper(result: Boolean, l: List[T]): Boolean = {
		if (!result || l == Nil) result
		else helper(pred(l.head), l.tail)
	}
	helper(true, list)
}

println("7-3. Assert List - Tail")
println(assertListTail(List(1, 3, 5, 7), isOdd))
println(assertListTail(List(1, 3, 5, 6), isOdd))
println(assertListTail(List(1, 2, 5, 7), isOdd))

def assertListPipe[T](list: List[T], pred: T => Boolean): Boolean =
	list.filter(pred).size == list.size

println("7-4. Assert List - Pipe")
println(assertListPipe(List(1, 3, 5, 7), isOdd))
println(assertListPipe(List(1, 3, 5, 6), isOdd))
println(assertListPipe(List(1, 2, 5, 7), isOdd))

/*
**  13. Stream
*/

def infStream: LazyList[Int] = 1 #:: infStream

println("13-1. Infinitely Long Stream")
val s1 = infStream
println(s1.head)
println(s1.tail.head)
println(s1.tail.tail.head)

def nonNeg(n: Int = 0): LazyList[Int] = n #:: nonNeg(n + 1)

println("13-2. Non Negative Numbers")
val s2 = nonNeg()
println(s2.head)
println(s2.tail.head)
println(s2.tail.tail.head)

def nonNegEven(n: Int = 0): LazyList[Int] = n #:: nonNegEven(n + 2)

println("13-3. Non Negaative Even Numbers")
val s3 = nonNegEven()
println(s3.head)
println(s3.tail.head)
println(s3.tail.tail.head)

def sqrs(n: Int = 0): LazyList[Int] = (n * n) #:: sqrs(n + 1)

println("13-2. Squares of Numbers")
val s4 = sqrs()
println(s4.head)
println(s4.tail.head)
println(s4.tail.tail.head)

/*
**  15. Spell Check
*/

def spellCheck(doc: List[String], dict: List[String]): List[String] = {
	doc.filter((word: String) => !dict.contains(word))
}

println("15. Spell Check")
println(spellCheck(List("CS152", "is", "fun"), List("CS152", "fun")))

/*
**  16. Polynomial and Monomial
*/

def evalMono(mono: (Double, Double), x: Double): Double = mono._1 * pow(x, mono._2)

def evalPoly(poly: List[(Double, Double)], x: Double): Double =
	if (poly == Nil) 0 else poly.map(evalMono(_, x)).reduce(_ + _)

println("16. Polynomial and Monomial")
evalMono((2.0, 3.0), 5.0)
evalPoly(List((2.0, 3.0), (4.0, 5.0)), 6.0)