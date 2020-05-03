package expression

import context._
import value._

case class FunCall(val operator: Identifier, val operands: List[Expression]) extends Expression {
  def execute(env: Environment): Value = {
    val args: List[Value] = flags.paramPassing match {
      case flags.BY_NAME => operands.map(new Thunk(_, env))
      case flags.BY_TEXT => operands.map(new Text(_))
      case flags.BY_VALUE => operands.map(_.execute(env))
    }
    try {
      alu.execute(operator, args)
    }
    catch {
      case e: UndefinedException =>
        operator.execute(env) match {
          case thunk: Thunk => thunk()
          case closure: Closure => closure(args)
          case _ => throw e
        }
    }
    /*
    if (env.contains(operator)) {
      operator.execute(env) match {
        case f: Thunk => f(Nil)
        case f: Closure => f.apply(args, env)
        case _ => throw new TypeException("Only functions can be called")
      }
    }
    else {
      if (flags.paramPassing != flags.BY_VALUE)
        alu.execute(operator, operands.map(_.execute(env)))
      else alu.execute(operator, args)
    }
    try {
      alu.execute(operator, args)
    }
    catch {
      case e: UndefinedException =>
        operator.execute(env) match {
          case thunk: Thunk => thunk(env)
          case closure: Closure => closure(args)
          case _ => throw e
        }
    }
     */
  }
}