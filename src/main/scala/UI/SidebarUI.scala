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

class SidebarUI(pane: Pane, mapInst: GameMap, variatesRef: ObjectProperty[Map[String, Double]], towersOnMap: BufferProperty[Tower]) extends VBox {
    val regularTower = new TowerButtonUI(REGULAR_TOWER_LOC, R_NAME, R_COST, "What can a school kid do?", pane, mapInst, variatesRef, towersOnMap)
    val slowDownTower = new TowerButtonUI(SLOW_DOWN_TOWER_LOC, S_NAME, S_COST, "Slows down profits.", pane, mapInst, variatesRef, towersOnMap)
    // Label for showing money amount
    val moneyLabel = new Label(variatesRef.value("money").toString()) {
        style = "-fx-font: normal bold 20 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;"
    }

    // Update money label
    variatesRef.onChange((_, _, newValue) => {
        moneyLabel.text = newValue("money").toString()
    })

    padding = Insets(20)
    spacing = 10
    children = Seq(
        regularTower,
        slowDownTower,
        moneyLabel
    )
    // set sidebar width
    prefWidth = SIDEBAR_WIDTH
    style = "-fx-background-color: grey;"
}
