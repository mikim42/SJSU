/*
**  CS152 - Assignment 5
**  Mingyun Kim
*/
object AcornLab extends App {

	abstract class Expression {
		def execute: Double
	}

	class Sum(val op1: Expression, val op2: Expression) extends Expression {
		def execute: Double = op1.execute + op2.execute

		override def toString = "(+ " + op1 + " " + op2 + ")"
	}

	class Number(val value: Double) extends Expression {
		def execute: Double = value

		override def toString = value.toString()
	}

	class Product(val op1: Expression, val op2: Expression) extends Expression {
		def execute: Double = op1.execute * op2.execute

		override def toString = "(* " + op1 + " " + op2 + ")"
	}

	object Sum {
		def apply(op1: Expression, op2: Expression) = new Sum(op1, op2)
	}

	object Number {
		def apply(value: Double) = new Number(value)
	}

	object Product {
		def apply(op1: Expression, op2: Expression) = new Product(op1, op2)
	}

	var exp: Expression = Sum(Number(42), Product(Number(3.14), Number(2.71)))
	println("the value of " + exp + " = " + exp.execute)
	exp = Product(Number(2), Product(Number(3), Number(5)))
	println("the value of " + exp + " = " + exp.execute)
}