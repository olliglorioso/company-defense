package Util

import scalafx.beans.property._
import scalafx.stage.Screen

object State {
  val variates = ObjectProperty(
    Map (
      "money" -> 500.0, 
      "health" -> 10.0, 
      "waveNo" -> 0.0, 
      "score" -> 0.0 
    )
  )
  val difficulty = IntegerProperty(1)
  val screenHeight = DoubleProperty(0.0)
  val screenWidth = DoubleProperty(0.0)
}
