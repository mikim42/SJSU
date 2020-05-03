package value

import context._
import expression._

class Thunk(body: Expression, defEnv: Environment) extends Closure(Nil, body, defEnv) {
  private var cache: Value = null
  /*
  def apply(callEnv: Environment) = {
    if (cache == null)
      cache = super.apply(Nil, callEnv)
    cache
  }
   */
  def apply(): Value = {
    if (cache == null)
      cache = super.apply(Nil)
    cache
  }
}