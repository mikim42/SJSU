package expression

import value._
import context._

case class Declaration(val iden: Identifier, val exp: Expression) extends SpecialForm {
  def execute(env: Environment) = {
    val result = exp.execute(env)
    env.put(iden, result)
    Notification.OK
  }
}