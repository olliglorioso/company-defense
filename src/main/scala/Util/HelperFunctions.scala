package Util

import Util.Constants._
import scalafx.scene.Scene
import scalafx.scene.Node
import UI.GameplayUI
import Util.State.difficulty
import ujson.Num

object HelperFunctions {
  def getTowerDisplayName(path: String) = 
    path match
      case REGULAR_TOWER_LOC => R_NAME
      case SLOW_DOWN_TOWER_LOC => S_NAME
      case _ => "Unknown"
  
  def showMessage(message: String, title: String, blinks: Int, node: Node) = 
    val currentScene: GameplayUI = node.getScene().asInstanceOf[GameplayUI]
    currentScene.showMessage(message, title, blinks)
  
  def labelStyle(size: Int, color: String = "orange"): String = s"-fx-font: normal bold ${size} Langdon; -fx-base: #AE3522; " + s"-fx-text-fill: ${color};"
  def getMap = features(difficulty.value)._4
  def getWaveData = features(difficulty.value)._3
  def getHealth = features(difficulty.value)._1
  def getMoney = features(difficulty.value)._2
  def anyToInteger(x: Any) = x.toString().toInt
  def getDifficultyName = difficulty.value match
    case 1 => "Reijon Maansiirto Tmi"
    case 2 => "IBM & Cisco & BlackBerry"
    case 3 => "MAGAT"
    case _ => "Nokia (Custom)"
}
