{
	object poly {
		def roots(p: (Double, Double, Double)): Option[(Double, Double)] = {
			// = None if p has no real roots
			// = Some((r1, r2)) where p(r1) == p(r2) == 0
			var disc = (p._2 * p._2) - (4 * p._1 * p._3)
			if (disc % 1.0 == 0)
				Some(((-p._2 + Math.sqrt(disc)) / (2 * p._1),
					(-p._2 - Math.sqrt(disc)) / (2 * p._1)))
			else
				None
		}

		def deriv(p: (Double, Double, Double)): (Double, Double, Double) = {
			(0, p._1 * 2, p._2)
		}

		def eval(a: Double, p: (Double, Double, Double)): Double = {
			// = p(a)
			(p._1 * a * a) + (p._2 * a) + p._3
		}
	}
	val p = (3.0, 9.0, -30.0) // = (3x - 6) * (x + 5)
	println("1. poly.scala")
	println("eval(6, p) = " + poly.eval(6, p))
	println("eval(2, p) = " + poly.eval(2, p))
	println("eval(-5, p) = " + poly.eval(-5, p))
	println("roots(p) = " + poly.roots(p))
	println("deriv(p) = " + poly.deriv(p))
	println("deriv2(p) = " + poly.deriv(poly.deriv(p)))

	/*
	**  I would make an object class that can represent i such that i * i = -1
	**  I would put functions that repeats multiplication depends on how many
	**  i's are there. Then, return appropriate value
	**  To alter poly.scala to work with complex numbers, I need to check if
	**  the discriminant is less than 0, then return with i
	**  To make it happen, the return type should be changed from
	**  Option[(Double, Double)] to something else that including i
	*/
}
{
	object vector {
		def sum(v1: (Double, Double, Double), v2: (Double, Double, Double)): (Double, Double, Double) =
			(v1._1 + v2._1, v1._2 + v2._2, v1._3 + v2._3)

		def mul(a: Double,v: (Double, Double, Double)): (Double, Double, Double) =
			(v._1 * a, v._2 * a, v._3 * a)

		def dot(v1: (Double, Double, Double), v2: (Double, Double, Double)): Double =
			v1._1 * v2._1 + v1._2 * v2._2 + v1._3 * v2._3

		def length(v: (Double, Double, Double)): Double =
			Math.sqrt(v._1 * v._1 + v._2 * v._2 + v._3 * v._3)

		def theta(v1: (Double, Double, Double), v2: (Double, Double, Double)): Double =
			Math.acos(dot(v1, v2) / (length(v1) * length(v2)))
	}
	val v1 = (2.0, 2.0, 2.0)
	val v2 = (1.0, 0.0, 0.0)
	val v3 = (0.0, 1.0, 0.0)

	println("2. vector.scala")
	println("sum(v3, v2) = " + vector.sum(v3, v2))
	println("mul(3, v1) = " + vector.mul(3, v1))

	println("dot(v1, v2) = " + vector.dot(v1, v2))
	println("dot(v2, v3) = " + vector.dot(v2, v3))
	println("dot(v1, v1) = " + vector.dot(v1, v1))

	println("length(v1) = " + vector.length(v1))
	println("length(v2) = " + vector.length(v2))

	println("theta(v1, v2) = " + vector.theta(v1, v2))
	println("theta(v3, v2) = " + vector.theta(v3, v2))
	println("pi/2 = " + Math.PI/2)
	/*
	**  To make it work with n-dimensional vectors, I would need to make it
	**  List and iterate through the lists to calculate answers
	**  For matrices, I would need List of List or 2D array
	**  I would include functions that handle matrices in 2D array since it is
	**  easier to manage the code
	*/
}
{
	object arithmetic {
		def sqrt(n: Int): Option[Int] = {
			// = None if n < 0
			// = largest int m such that m * m <= n
			if (n < 0) return None
			var result = 0
			for (i <- 0 to n if i * i <= n)
				if (i * i <= n) result = i
			Some(result)
		}

		def log(n: Int): Option[Int] = {
			// = None if N <= 0
			// = largest m such that 2^m <= n
			if (n <= 0) return None
			var result = 0
			for (i <- 1 to n if Math.pow(2, i) <= n)
				if (Math.pow(2, i) % 1.0 <= n) result = i
			Some(result)
		}

		def isPrime(n: Int): Option[Boolean] = {
			// = true if n has no divisors > 1
			for (i <- 2 to n / 2)
				if ( n % i == 0) return Some(false)
			Some(true)
		}

		def gcd(n: Int, m: Int): Option[Int] = {
			// = None if n or m < 0
			// = gcd(m, n) if n < m
			// = largest k dividing both n and m
			if (n < 0 || m < 0) return None
			if (n < m) return gcd(m, n)
			var a = n
			var b = m
			while (b > 0) {
				var t = b
				b = a % b
				a = t
			}
			Some(a)

		}

		def lcm(n: Int, m: Int): Option[Int] = {
			// = None if n < 0 or m < 0
			// = smallest k such that n a,d m divide k
			if (n < 0 || m < 0) return None
			if (n < m) return lcm(m, n)
			var t = n
			while (t < n * m) {
				if (t % m == 0)
					return Some(t)
				t += n
			}
			Some(n * m)
		}

		def phi(n: Int): Option[Int] = {
			// = None if n < 0
			// = # of k <= n such that gcd(k, n) = 1
			if (n < 0) return None
			var result = 0
			for (i <- 1 to n if gcd(i, n) == Some(1)) result += 1
			Some(result)
		}

	}
	println("3. arithmetic.scala")
	println("gcd(15, 12) = " + arithmetic.gcd(15, 12))
	println("lcm(15, 12) = " + arithmetic.lcm(15, 12))
	println("gcd(13, 12) = " + arithmetic.gcd(13, 12))
	println("gcd(-13, 12) = " + arithmetic.gcd(-13, 12))
	println("phi(9)= " + arithmetic.phi(9))
	println("sqrt(49) = " + arithmetic.sqrt(49))
	println("sqrt(37) = " + arithmetic.sqrt(37))
	println("sqrt(35) = " + arithmetic.sqrt(35))
	println("log(64) = " + arithmetic.log(64))
	println("log(130) = " + arithmetic.log(130))
	println("log(9) = " + arithmetic.log(9))
	println("log(0) = " + arithmetic.log(0))
	println("isPrime(23) = " + arithmetic.isPrime(23))
	println("isPrime(59) = " + arithmetic.isPrime(59))
	println("isPrime(75) = " + arithmetic.isPrime(75))
	/*
	**  To find prime number, we iterate only until n / 2 because it is
	**  unnecessary to check n / 2 - n. To improve this, we can do k + 2
	**  instead of k++ from 3 to avoid 2's multiples
	*/
}
{
	def rollDice(): (Int, Int) = (((Math.random() * 100).toInt % 6 + 1),
	((Math.random() * 100).toInt % 6 + 1))

	println("4. Roll Dice")
	println(rollDice())
	println(rollDice())
	println(rollDice())
	println(rollDice())
	println(rollDice())
	/*
	**  Math.random return value between 0.0 - 1.0 so I can do some calculation
	**  to make it different number (I did * 100), then use modulo to get the
	**  randomly generated value
	**  it might repeat the same sequence of random numbers since random numbers
	**  are actually pre-defined. To avoid this repetition, I can seed the
	**  random function and rearrange initial position such as new Random(Int)
	*/
}
