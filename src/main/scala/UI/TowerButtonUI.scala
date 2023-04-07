package UI

import scalafx.scene.image._
import scalafx.scene.control._
import scala.concurrent.duration.Duration
import javafx.scene.input.MouseEvent
import Logic.Tower
import scalafx.scene.layout._
import scalafx.scene.shape.Circle
import Util.Constants._
import Logic.GameMap

class TowerButtonUI(picLoc: String, name: String, price: Int, desc: String, pane: Pane, mapInst: GameMap, var variates: Map[String, Double]) extends Button {
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
    minWidth = TOWER_SIDE
    minHeight = TOWER_SIDE
    tooltip_=(tt)
    onMousePressed = (event: MouseEvent) => {
        // Record initial position and mouse position
        userData = (translateX(), translateY(), event.getSceneX(), event.getSceneY())
    }

    def calculateNewPosition(event: MouseEvent, userData: Any): (Double, Double) = {
        val (x, y, mouseX, mouseY) = userData.asInstanceOf[(Double, Double, Double, Double)]
        val deltaX = mouseX - event.getSceneX()
        val deltaY = mouseY - event.getSceneY()
        (x - deltaX, y - deltaY)
    }

    def getButtonStyle(event: MouseEvent, mapInst: GameMap): String = {
        if (mapInst.isBgTile(event.getSceneY(), event.getSceneX())) {
            "-fx-background-color: green;"
        } else {
            "-fx-background-color: red;"
        }
    }
    onMouseDragged = (event: MouseEvent) => {
        if (variates("money") < price) {
            print("not enough money")
        } else {
            val newPos = calculateNewPosition(event, userData)
            translateX = newPos._1
            translateY = newPos._2
            style = getButtonStyle(event, mapInst)
        }
    }

    onMouseReleased = (event: MouseEvent) => {
        val (x, y, mouseX, mouseY) = userData.asInstanceOf[(Double, Double, Double, Double)]
        // Illegal positions not allowed
        if (!mapInst.isBgTile(event.getSceneY(), event.getSceneX())) {
            println("Illegal position")
            translateX = x
            translateY = y
            style = "-fx-background-color: transparent;"
        } else {
            // Create a new Tower instance in this position
            val newTower = new Tower(picLoc, TOWER_SIDE, name, price)
            // make tooltip to stay when mouse over it
            val info = new Button() {
                tooltip_=(tt)
                minWidth = TOWER_SIDE
                minHeight = TOWER_SIDE
            }
            val stackPane = new StackPane()
            info.style = "-fx-background-color: transparent; -fx-border-color: transparent;" // Tooltip button style (transparent)
            stackPane.getChildren().addAll(newTower, info)
            stackPane.translateX = event.getSceneX() - (minWidth() / 2)
            stackPane.translateY = event.getSceneY() - (minHeight() / 2)
            // Return the button to its original position
            variates = variates.updated("money", variates("money") - price)
            println(variates("money"))
            pane.children.add(stackPane)
            translateX = x
            translateY = y
            style = "-fx-background-color: transparent;"
        }
        
    }
}
