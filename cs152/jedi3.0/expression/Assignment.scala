package expression

import context._
import value._

case class Assignment(val v: Identifier, val update: Expression) extends SpecialForm {
  def execute(env: Environment): Value = {
    v.execute(env) match {
      case variable: Variable => variable.content = update.execute(env)
      case _ => throw new TypeException("Can only modify Variable.")
    }
    Notification.DONE
  }
}
