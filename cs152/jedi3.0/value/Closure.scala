package value

import context._
import expression._

class Closure(val params: List[Identifier], val body: Expression, val defEnv: Environment) extends Value {
  def apply(args: List[Value], callEnv: Environment = null): Value = {
    var tempEnv: Environment = null
    if (callEnv != null) tempEnv = new Environment(callEnv)
    else tempEnv = new Environment(defEnv)
    tempEnv.bulkPut(params, args)
    body.execute(tempEnv)
  }
}
