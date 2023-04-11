package Util

import scalafx.beans.property.ObjectProperty

object State {
  val variates = ObjectProperty(
    Map("money" -> 500.0, "health" -> 10.0, "waveNo" -> 0.0, "score" -> 0.0)
  )
}
