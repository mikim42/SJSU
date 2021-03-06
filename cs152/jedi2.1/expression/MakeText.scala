package expression

import context._
import value._

case class MakeText(val expression: Expression) extends SpecialForm {
  def execute(env: Environment) = new Text(expression)
}
