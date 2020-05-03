package expression

import context._

case class FunCall(op: Identifier, args: List[Expression]) extends Expression {
  def execute(env: Environment) = {
    val args2 = args.map(_.execute(env))
    alu.execute(op, args2)
  }
}