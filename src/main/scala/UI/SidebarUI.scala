package UI

import Util.Constants._
import scalafx.geometry.Insets
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
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control._
import scalafx.beans.property.BufferProperty
import scalafx.geometry.Rectangle2D
import scalafx.stage.Screen
import scalafx.scene.paint.Color
import scalafx.scene.image.ImageView
import Logic._
import scalafx.geometry.Pos
import Util.HelperFunctions.labelStyle

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
    showMessage,
    openUpgradeMenu
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
    showMessage,
    openUpgradeMenu
  )

  val visualBounds: Rectangle2D = Screen.primary.visualBounds
  val (h, w) = (visualBounds.getHeight, visualBounds.getWidth)

  def openUpgradeMenu(tower: Tower): Unit = {
    val levelLabel = new Label(s"Level: ${tower.level.value}") {
      style = labelStyle(13)
    }

    val sellButton = new Button(s"Sell (${tower.sellPrice})") {
          style = "-fx-base: red;"
          prefWidth = 100
          onAction = _ => {
            variatesRef.value = variatesRef.value.updated("money", variatesRef.value("money") + tower.sellPrice)
            moneyLabel.text = variatesRef.value("money").toInt.toString()
            pane.children.remove(tower)
            towersOnMap.value -= tower
            children = Seq(towers, infoLabels)
          }
        }
    var editInfo = new VBox() {
      alignment = Pos.Center
      spacing = 10
      children = Seq(
        new Label(tower.displayName) {
          style = labelStyle(13)
        },
        levelLabel,
        new Button(s"Up (${tower.upgradePrice})") {
          style = "-fx-base: green;"
          prefWidth = 100
          onAction = _ => {
            val newMoney = tower.upgrade(variatesRef.value("money"))
            variatesRef.value = variatesRef.value.updated("money", newMoney)
            moneyLabel.text = variatesRef.value("money").toInt.toString()
            this.text = s"Up (${tower.upgradePrice})"
            levelLabel.text = s"Level: ${tower.level.value}"
            sellButton.text = s"Sell (${tower.sellPrice})"
          }
        },
        sellButton
      )
    }
    children = Seq(
      towers,
      editInfo,
      infoLabels
    )
  }
  // Label for showing money amount
  val moneyLabel = new Label(variatesRef.value("money").toInt.toString()) {
    style = labelStyle(20)
    graphic = new ImageView(MONEY_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  }

  val healthLabel = new Label(variatesRef.value("health").toInt.toString()) {
    style = labelStyle(20)
    graphic = new ImageView(HEALTH_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  }
  val scoreLabel = new Label(variatesRef.value("score").toInt.toString()) {
    style = labelStyle(20)
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
  val towers = new VBox() {
    alignment = Pos.Center
    children = Seq(
      regularTower,
      slowDownTower
    )
    spacing = 10
    style = "-fx-background-color: grey;"
  }

  val infoLabels = new VBox() {
    // set to the bottom of sidebar
    alignment = Pos.BottomRight
    translateY = h - UI_TILE_SIZE * towers.children.length - 3 * 75
    children = Seq(
      moneyLabel,
      healthLabel,
      scoreLabel
    )
    spacing = 10
    style = "-fx-background-color: grey;"
  }

  padding = Insets(20)
  spacing = 10
  children = Seq(
    towers,
    infoLabels
  )

  prefWidth = SIDEBAR_WIDTH
  style = "-fx-background-color: grey;"
}
