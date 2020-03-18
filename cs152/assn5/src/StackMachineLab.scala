import scala.collection.mutable.ListBuffer

/*
**  CS152 - Assignment 5
**  Mingyun Kim
*/
object StackMachineLab extends App {

	object StackMachine {
		var stack = new ListBuffer[Int]
		var program = List[Command]()

		def run = {
			stack.clear
			for (cmd <- program) cmd.execute
		}

		def clear = {
			stack.clear
		}

		def push(x: Int): Unit = stack += x

		def pop = {
			if (stack.size > 0) stack.remove(0)
			else throw new Exception("Stack is empty")
		}

		def top = stack(0)

		def sum = {
			if (stack.size < 2) throw new Exception("Not enough elements")
			stack(1) += stack(0)
			stack.remove(0)
		}

		def times = {
			if (stack.size < 2) throw new Exception("Not enough elements")
			stack(1) *= stack(0)
			stack.remove(0)
		}
	}

	trait Command {
		def execute
	}

	class Push(x: Int) extends Command {
		def execute = StackMachine.push(x)
	}

	class Pop extends Command {
		def execute = StackMachine.pop
	}

	class Top extends Command {
		def execute = println(StackMachine.top)
	}

	class Sum extends Command {
		def execute = StackMachine.sum
	}

	class Times extends Command {
		def execute = StackMachine.times
	}

	object Pop {
		def apply() = new Pop
	}

	object Push {
		def apply(x: Int) = new Push(x)
	}

	object Top {
		def apply() = new Top
	}

	object Sum {
		def apply() = new Sum
	}

	object Times {
		def apply() = new Times
	}

	StackMachine.clear
	StackMachine.program = List(Push(3), Push(5), Sum(), Top())
	StackMachine.run
}
