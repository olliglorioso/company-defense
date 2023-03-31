package UI

import scalafx.scene.layout.VBox
import Util.Constants._
import scalafx.geometry.Insets
import Logic.Tower
import scalafx.scene.control.Tooltip
import scalafx.scene.layout.Pane
import scalafx.scene.layout.BorderPane
import scalafx.scene.input.MouseEvent
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.event.EventIncludes.eventClosureWrapperWithZeroParam
import scalafx.scene.input.ClipboardContent

class SidebarUI(mapPane: Pane, root: BorderPane) extends VBox {
    val regularTower = new TowerButtonUI(REGULAR_TOWER_LOC, R_NAME, R_COST, "What can a school kid do?")
    val slowDownTower = new TowerButtonUI(SLOW_DOWN_TOWER_LOC, S_NAME, S_COST, "Slows down profits.")
    


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
