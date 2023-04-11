package Util

import Util.Constants._
import scalafx.scene.Scene
import scalafx.scene.Node
import UI.GameplayUI

object HelperFunctions {
  def getTowerDisplayName(path: String) = 
    path match
      case REGULAR_TOWER_LOC => R_NAME
      case SLOW_DOWN_TOWER_LOC => S_NAME
      case _ => "Unknown"
  
  def showMessage(message: String, title: String, blinks: Int, node: Node) = 
    val currentScene: GameplayUI = node.getScene().asInstanceOf[GameplayUI]
    currentScene.showMessage(message, title, blinks)
  
  def labelStyle(size: Int): String = s"-fx-font: normal bold ${size} Langdon; -fx-base: #AE3522; " + "-fx-text-fill: orange;"
}
