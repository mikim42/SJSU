package expression

import value._
import context._

case class Conditional(val cond: Expression, val cons: Expression, val alter: Expression = null) extends SpecialForm {
  def execute(env: Environment): Value = {
    if (cond.execute(env) == Boole(true)) cons.execute(env)
    else {
      if (alter == null) Notification.UNSPECIFIED
      else alter.execute(env)
    }
  }
}
