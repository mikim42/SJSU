/*
**  CS152 - Assignment 5
**  Mingyun Kim
*/
object AccumulatorLab extends App {

	object Accumulator {
		var register = 0
		var program: List[Instruction] = List[Instruction]()
		var halt = false
		var ip = 0

		def clear = {
			register = 0
			ip = 0
			halt = false
		}

		def run(): Unit = {
			while (ip < program.size && !halt) {
				program(ip).execute
				ip += 1
			}
		}
	}

	trait Instruction {
		def execute
	}

	class Add(val x: Int) extends Instruction {
		def execute = Accumulator.register += x
	}

	class Mul(val x: Int) extends Instruction {
		def execute = Accumulator.register *= x
	}

	class Halt() extends Instruction {
		def execute = Accumulator.halt = true
	}

	class Goto(val arg: Int) extends Instruction {
		def execute = Accumulator.ip = arg - 1
	}

	object Add {
		def apply(x: Int) = new Add(x)
	}

	object Mul {
		def apply(x: Int) = new Mul(x)
	}

	object Halt {
		def apply() = new Halt
	}

	object Goto {
		def apply(arg: Int) = new Goto(arg)
	}

	Accumulator.clear
	Accumulator.program = List(Add(3), Mul(5), Add(1), Mul(2))
	Accumulator.run()
	println(Accumulator.register)
	Accumulator.clear
	Accumulator.program = List(Add(10), Mul(2), Add(3), Mul(5))
	Accumulator.run()
	println(Accumulator.register)
	Accumulator.clear
	Accumulator.program = List(Add(10), Mul(2), Halt(), Add(3), Mul(5))
	Accumulator.run()
	println(Accumulator.register)
	Accumulator.clear
	Accumulator.program = List(Goto(3), Add(10), Mul(2), Add(3), Mul(5))
	Accumulator.run()
	println(Accumulator.register)
}
