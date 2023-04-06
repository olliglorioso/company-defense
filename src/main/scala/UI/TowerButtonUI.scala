package UI

import scalafx.scene.control.Button
import scalafx.scene.image._
import scalafx.scene.control.Tooltip
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.event.EventIncludes.eventClosureWrapperWithZeroParam
import scala.concurrent.duration.Duration
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import javafx.scene.input.MouseEvent
import Logic.Tower
import scalafx.scene.layout.Pane
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.HBox
import scalafx.scene.shape.Circle
import Util.Constants
import Logic.GameMap

class TowerButtonUI(picLoc: String, name: String, price: Int, desc: String, pane: Pane, mapInst: GameMap) extends Button {
    val image = new Image(picLoc)
    val originalPos = (layoutX, layoutY)
    val imageView = new ImageView(image) {
        fitWidth = 80
        fitHeight = 80
        preserveRatio = true
    }
    val ttstyle = ("-fx-font: normal bold 20 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;")
    val tt = new Tooltip()
    tt.setStyle(ttstyle)
    tt.setGraphic(new VBox {
        spacing = 5
        children = Seq(
            new Label(name) {
                style = ttstyle
            },
            new Label("Price: " + price),
            new Label(desc)
        )
    })
    tt.setShowDelay(javafx.util.Duration(650.0))
    style = "-fx-background-color: transparent;"
    graphic = imageView
    minWidth = Constants.TOWER_SIDE
    minHeight = Constants.TOWER_SIDE
    tooltip_=(tt)
    onMousePressed = (event: MouseEvent) => {
        // Record initial position and mouse position
        userData = (translateX(), translateY(), event.getSceneX(), event.getSceneY())
    }
    onMouseDragged = (event: MouseEvent) => {
        // Calculate new position
        val (x, y, mouseX, mouseY) = userData.asInstanceOf[(Double, Double, Double, Double)]
        val deltaX = mouseX - event.getSceneX()
        val deltaY = mouseY - event.getSceneY()
        // find the fill color of the square in the current event.getSceneX(). there are rows and columns of squares side length 89.5
        translateX = x - deltaX
        translateY = y - deltaY
        if (mapInst.isBgTile(event.getSceneY(), event.getSceneX())) {
            // just set the theme of the button to green
            style = "-fx-background-color: green;"
        } else {
            style = "-fx-background-color: red;"
        }
    }
    onMouseReleased = (event: MouseEvent) => {
        
        val (x, y, mouseX, mouseY) = userData.asInstanceOf[(Double, Double, Double, Double)]
        // Illegal positions not allowed
        if (!mapInst.isBgTile(event.getSceneY(), event.getSceneX())) {
            translateX = x
            translateY = y
            style = "-fx-background-color: transparent;"
        } else {
            // Create a new Tower instance in this position
            val newTower = new Tower(picLoc, Constants.TOWER_SIDE, name, price)
            // make tooltip to stay when mouse over it
            tt.setConsumeAutoHidingEvents(false)
            tt.setAutoHide(false)

            val info = new Button() {
                tooltip_=(tt)
                minWidth = Constants.TOWER_SIDE
                minHeight = Constants.TOWER_SIDE
            }
            val stackPane = new StackPane()
            info.style = "-fx-background-color: transparent; -fx-border-color: transparent;" // Tooltip button style (transparent)
            stackPane.getChildren().addAll(newTower, info)
            stackPane.translateX = event.getSceneX() - (minWidth() / 2)
            stackPane.translateY = event.getSceneY() - (minHeight() / 2)
            // Return the button to its original position
            
            pane.children.add(stackPane)
            translateX = x
            translateY = y
            style = "-fx-background-color: transparent;"
        }
        
    }
}
