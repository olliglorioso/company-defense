package UI

import scalafx.scene.layout.VBox
import Util.Constants._
import scalafx.geometry.Insets
import Logic.Tower

class SidebarUI extends VBox {
    val regularTower = new TowerButtonUI(REGULAR_TOWER_LOC)
    val slowDownTower = new TowerButtonUI(SLOW_DOWN_TOWER_LOC)
    val tower = new Tower(REGULAR_TOWER_LOC, 80, "RegularTower", 10)
    regularTower.toFront()
    slowDownTower.toFront()
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
