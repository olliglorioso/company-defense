package UI

import Util.Constants._
import scalafx.geometry.Insets
import Logic.Tower
import scalafx.scene.control.Tooltip
import scalafx.scene.layout._
import javafx.scene.input.MouseEvent

import scalafx.Includes.eventClosureWrapperWithParam
import scalafx.event.EventIncludes.eventClosureWrapperWithParam
import scalafx.scene.input.ClipboardContent
import scalafx.scene.text.Text
import scalafx.scene.shape.Rectangle
import scalafx.scene.input.TransferMode
import scalafx.scene.Scene
import Logic.GameMap
import scalafx.scene.control.Label
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.Labeled
import scalafx.beans.property.BufferProperty
import scalafx.geometry.Rectangle2D
import scalafx.stage.Screen
import scalafx.scene.paint.Color
import scalafx.scene.image.ImageView

class SidebarUI(
    pane: Pane,
    mapInst: GameMap,
    variatesRef: ObjectProperty[Map[String, Double]],
    towersOnMap: BufferProperty[Tower],
    showMessage: (String, String, Int) => Unit
) extends VBox {
  val regularTower = new TowerButtonUI(
    REGULAR_TOWER_LOC,
    R_NAME,
    R_COST,
    "What can a school kid do?",
    pane,
    mapInst,
    variatesRef,
    towersOnMap,
    showMessage
  )
  val slowDownTower = new TowerButtonUI(
    SLOW_DOWN_TOWER_LOC,
    S_NAME,
    S_COST,
    "Slows down profits.",
    pane,
    mapInst,
    variatesRef,
    towersOnMap,
    showMessage
  )

  val visualBounds: Rectangle2D = Screen.primary.visualBounds
  val (h, w) = (visualBounds.getHeight, visualBounds.getWidth)
  // Label for showing money amount
  val moneyLabel = new Label(variatesRef.value("money").toInt.toString()) {
    style =
      "-fx-font: normal bold 20 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;"
    translateY = h - 100
    graphic = new ImageView(MONEY_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  }

  val healthLabel = new Label(variatesRef.value("health").toInt.toString()) {
    style =
      "-fx-font: normal bold 20 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;"
    translateY = h - 175
    graphic = new ImageView(HEALTH_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  }

  val scoreLabel = new Label(variatesRef.value("score").toInt.toString()) {
    style =
      "-fx-font: normal bold 20 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;"
    translateY = h - 250
    // add an icon to the beginning of this label
    graphic = new ImageView(SCORE_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  }

  // Update money label
  variatesRef.onChange((_, _, newValue) => {
    moneyLabel.text = newValue("money").toInt.toString()
    scoreLabel.text = newValue("score").toInt.toString()
    healthLabel.text = newValue("health").toInt.toString()
  })
    

  padding = Insets(20)
  spacing = 10
  children = Seq(
    moneyLabel,
    healthLabel,
    scoreLabel,
    regularTower,
    slowDownTower,
  )
  // set sidebar width
  prefWidth = SIDEBAR_WIDTH
  style = "-fx-background-color: grey;"
}
