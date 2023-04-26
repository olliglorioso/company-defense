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
import scalafx.scene.paint.Color
import scalafx.scene.image.ImageView
import Logic._
import scalafx.geometry.Pos
import Util.HelperFunctions.labelStyle
import Util.State._
import scala.collection.mutable.ArrayBuffer
class SidebarUI(
    pane: Pane,
    mapInst: GameMap,
    towersOnMap: ArrayBuffer[Tower],
    showMessage: (String, String, Int) => Unit,
    saveAndExit: () => Unit,
    exit: () => Unit
) extends VBox:
  val regularTower = new TowerButtonUI(
    REGULAR_TOWER_LOC,
    80,
    R_NAME,
    R_COST,
    "What can a school kid do?",
    pane,
    mapInst,
    towersOnMap,
    showMessage,
    openUpgradeMenu
  )
  val slowDownTower = new TowerButtonUI(
    SLOW_DOWN_TOWER_LOC,
    80,
    S_NAME,
    S_COST,
    "Slows down profits.",
    pane,
    mapInst,
    towersOnMap,
    showMessage,
    openUpgradeMenu
  )

  val bombTower = new TowerButtonUI(
    BOMB_TOWER_LOC,
    100,
    B_NAME,
    B_COST,
    "I make a big impact for the better!",
    pane,
    mapInst,
    towersOnMap,
    showMessage,
    openUpgradeMenu
  )

  val moneyTower = new TowerButtonUI(
    MONEY_TOWER_LOC,
    80,
    M_NAME,
    M_COST,
    "Prints money from thin air.",
    pane,
    mapInst,
    towersOnMap,
    showMessage,
    openUpgradeMenu
  )

  /**
    * Opens the upgrade/sell menu for the given tower.
    *
    * @param tower Tower to upgrade/sell.
    */
  def openUpgradeMenu(tower: Tower): Unit =
    val levelLabel = new Label(s"Level: ${tower.level.value}") {
      style = labelStyle(13)
    }

    val sellButton = new Button(s"Sell (${tower.sellPrice})") {
      style = "-fx-base: red;"
      prefWidth = 100
      onAction = _ => {
        variates.setValue(variates.value.updated("money", variates.value("money") + tower.sellPrice))
        moneyLabel.text = variates.value("money").toInt.toString()
        pane.children.remove(tower)
        towersOnMap -= tower
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
            val newMoney = tower.upgrade(variates.value("money"))
            variates.value = variates.value.updated("money", newMoney)
            moneyLabel.text = variates.value("money").toInt.toString()
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
      infoLabels,
      saveAndExitButton,
      exitButton
    )
  end openUpgradeMenu
  // Label for showing money amount
  val moneyLabel = new Label(variates.value("money").toInt.toString()):
    style = labelStyle(20)
    graphic = new ImageView(MONEY_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  end moneyLabel

  val healthLabel = new Label(variates.value("health").toInt.toString()):
    style = labelStyle(20)
    graphic = new ImageView(HEALTH_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  end healthLabel
  val scoreLabel = new Label(variates.value("score").toInt.toString()):
    style = labelStyle(20)
    graphic = new ImageView(SCORE_ICON_LOC) {
      fitWidth = 30.0
      fitHeight = 30.0
    }
  end scoreLabel
  // Update money label
  variates.onChange((_, _, newValue) => {
    moneyLabel.text = newValue("money").toInt.toString()
    scoreLabel.text = newValue("score").toInt.toString()
    healthLabel.text = newValue("health").toInt.toString()
  })
  val towers = new VBox():
    alignment = Pos.Center
    children = Seq(
      regularTower,
      slowDownTower,
      bombTower,
      moneyTower
    )
    spacing = 10
    style = "-fx-background-color: grey;"
  end towers

  val infoLabels = new VBox():
    // set to the bottom of sidebar
    alignment = Pos.BottomRight
    children = Seq(
      moneyLabel,
      healthLabel,
      scoreLabel
    )
    vgrow_=(Priority.Always)
    spacing = 10
    style = "-fx-background-color: transparent;"
  end infoLabels

  val saveAndExitButton = new Button("Save & exit"):
    style = "-fx-background-color: red; -fx-text-fill: black; -fx-font-size: 9pt; -fx-font-family: 'Arial Black', sans-serif; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
    prefWidth = 100
    vgrow_=(Priority.Always)
    alignment = Pos.BottomCenter
    onAction = _ => saveAndExit()
  end saveAndExitButton

  val exitButton = new Button("Exit"):
    style = "-fx-background-color: red; -fx-text-fill: black; -fx-font-size: 9pt; -fx-font-family: 'Arial Black', sans-serif; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
    prefWidth = 100
    vgrow_=(Priority.Always)
    alignment = Pos.BottomCenter
    onAction = _ => exit()
  end exitButton

  exitButton.id = "exitButton"

  padding = Insets(20)
  spacing = 10
  margin = Insets(0, 0, 30, 0)
  children = Seq(
    towers,
    infoLabels,
    saveAndExitButton,
    exitButton
  )

  prefWidth = SIDEBAR_WIDTH
  prefHeight = screenHeight.value
  style = "-fx-background-color: grey;"
end SidebarUI
