package value

import collection.mutable._
import context._

class Store(private var elems: ArrayBuffer[Value] = ArrayBuffer[Value]()) extends Value {
  // adds e to the end of store
  def add(e: Value) = elems += e
  // inserts e at position pos in this
  def put(e: Value, pos: Integer) = elems.insert(pos.v, e)
  // removes element at position pos from this
  def rem(pos: Integer) = elems.remove(pos.v)
  // returns element at position pos in this
  def get(pos: Integer): Value = elems(pos.v)
  // returns true ie this contains e
  def contains(e: Value): Boole = Boole(elems.contains(e))
  // returns the size of this
  def size: Integer = Integer(elems.size)
  // returns "{e0 e1 e2 ...}"
  override def toString = {
    var s = ""
    for (i <- elems.indices) {
      if (i == 0) s += s"${elems(i)}"
      else s += s", ${elems(i)}"
    }
    "{" + s + "}"
  }
  // returns store containing the elements of this transformed by trans
  def map(trans: Closure): Store = new Store(elems.map((v: Value) => trans(List(v))))
  // returns store containing the elements of this that passed test
  def filter(test: Closure): Store =
    new Store(ArrayBuffer(elems.toList.filter(x => test(List(x)).asInstanceOf[Boole].v): _*))
}