package context

object flags {
  var staticScope = true
  val BY_VALUE = 0
  val BY_NAME = 1
  val BY_TEXT = 2
  var paramPassing = BY_VALUE
}