package expression

import context._
import value._

/*
def pi = 3.14
def square = lambda(x) x * x
 */

case class Identifier(val name: String) extends Expression {
  def head = this
  override def equals(other: Any) =
    if (!other.isInstanceOf[Identifier]) false
    else this.name == other.asInstanceOf[Identifier].name
  override def hashCode = this.toString.hashCode
  override def toString = name
  def execute(env: Environment): Value =
    env(this) match {
      case thunk: Thunk => thunk()
      case text: Text => text(env)
      case value: Value => value
    }
}
