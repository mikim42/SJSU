package expression

import context._
import value._

case class Block(val expressions: List[Expression]) extends SpecialForm {
  def execute(env: Environment): Value = {
    val tempEnv = new Environment(env)
    var lastVal: Value = null
    for (expression <- expressions) lastVal = expression.execute(tempEnv)
    lastVal
  }
}
