package expression

import context._
import value._

case class Iteration(val cond: Expression, val body: Expression) extends SpecialForm {
  def execute(env: Environment): Value = {
    if (!cond.execute(env).isInstanceOf[Boole]) throw new TypeException
    while (cond.execute(env).asInstanceOf[Boole].v) {
      body.execute(env)
    }
    Notification.DONE
  }
}