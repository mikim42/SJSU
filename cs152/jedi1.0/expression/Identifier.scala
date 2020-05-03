package expression

import context._

/*
def pi = 3.14
def square = lambda(x) x * x
 */

case class Identifier(val name: String) extends Expression {
  override def toString = name
  def execute(env: Environment) = env(this)
}
