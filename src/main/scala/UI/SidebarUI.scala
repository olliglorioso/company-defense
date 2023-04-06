package UI

import scalafx.scene.layout.VBox
import Util.Constants._
import scalafx.geometry.Insets
import Logic.Tower
import scalafx.scene.control.Tooltip
import scalafx.scene.layout.Pane
import scalafx.scene.layout.BorderPane
import javafx.scene.input.MouseEvent

import scalafx.Includes.eventClosureWrapperWithParam
import scalafx.event.EventIncludes.eventClosureWrapperWithParam
import scalafx.scene.input.ClipboardContent
import scalafx.scene.text.Text
import scalafx.scene.shape.Rectangle
import scalafx.scene.input.TransferMode
import scalafx.scene.Scene
import Logic.GameMap

class SidebarUI(pane: Pane, root: BorderPane, mapInst: GameMap) extends VBox {
    val regularTower = new TowerButtonUI(REGULAR_TOWER_LOC, R_NAME, R_COST, "What can a school kid do?", pane, mapInst)
    val slowDownTower = new TowerButtonUI(SLOW_DOWN_TOWER_LOC, S_NAME, S_COST, "Slows down profits.", pane, mapInst)

    // make this node top one in the scene
    var previousMouseX = 0.0
    var previousMouseY = 0.0
    var first = true

    padding = Insets(20)
    spacing = 10
    children = Seq(
        regularTower,
        slowDownTower
    )
    // set sidebar width
    prefWidth = SIDEBAR_WIDTH
    style = "-fx-background-color: grey;"
}
