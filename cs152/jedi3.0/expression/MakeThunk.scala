package expression

import context._
import value._

case class MakeThunk(val expression: Expression) extends SpecialForm {
  def execute(env: Environment) = new Thunk(expression, env)
}
