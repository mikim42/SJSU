package value

import expression._
import context._

class Text(val body: Expression) extends Value {
  private var cache: Value = null
  def apply(callEnv: Environment) = {
    if (cache == null)
      cache = body.execute(callEnv)
    cache
  }
}