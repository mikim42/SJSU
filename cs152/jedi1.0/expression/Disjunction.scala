package expression

import value._
import context._

case class Disjunction(val ops: List[Expression]) extends SpecialForm {
  def execute(env: Environment): Value = {
    var result = false
    for (op <- ops if !result) if (op.execute(env) == Boole(true)) result = true
    Boole(result)
  }
}
