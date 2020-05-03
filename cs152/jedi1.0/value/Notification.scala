package value

class Notification(val name: String) extends Value {
  override def toString = name
}

object Notification {
  def apply(name: String) = new Notification(name)
  val OK = new Notification("OK")
  val UNSPECIFIED = new Notification("UNSPECIFIED")
  val DONE = new Notification("DONE")
}