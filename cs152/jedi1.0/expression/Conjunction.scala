package expression

import value._
import context._

case class Conjunction(val ops: List[Expression]) extends SpecialForm {
  def execute(env: Environment): Value = {
    var result = true
    for (op <- ops if result) if (op.execute(env) == Boole(false)) result = false
    Boole(result)
  }
}