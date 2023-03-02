package UI.Gameplay

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TitledPane}
import scalafx.scene.layout.VBox
import scalafx.scene.control.{SplitPane, ListView}
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import javafx.scene.Node
import scalafx.beans.property.ObjectProperty
import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle


class GameplayUI {
    val squareSide = 50
    /**
      * 
      *
      * @param stage PrimaryStage
      * @param w Width of the scene
      * @param h Height of the scene
      * @return Gameplay-scene
      */
    def gameplayScene(stage: JFXApp3.PrimaryStage, w: Double, h: Double): Scene =
        
        val mapWidth = 20 * squareSide
        val mapHeight = 12 * squareSide
        val map = createMap(mapWidth, mapHeight, 20, 12)
        val gameplayScene: Scene = new Scene(w, h) {
                root = new BorderPane {
                    right = sidebar()
                    center = new BorderPane {
                        center = new Pane {
                            children = map.flatten
                            prefWidth = mapWidth
                            prefHeight = mapHeight
                        }
                    }
                }
            }
        gameplayScene
    
    def sidebar(): VBox =
        val regularTower = towerButton("file:src/resources/RegularTower.png")
        val slowDownTower = towerButton("file:src/resources/SlowDownTower.png")

        val sidebar = new VBox {
            padding = Insets(20)
            spacing = 10
            children = Seq(
                new Label("Towers"),
                regularTower,
                slowDownTower
            )
            style = "-fx-background-color: grey;"
        }
        sidebar
    
    def towerButton(picLoc: String): Button =
        val image = new Image(picLoc)
        val imageView = new ImageView(image) {
            fitWidth = 100
            fitHeight = 100
            preserveRatio = true
        }

        val button = new Button {
            graphic = imageView
            minWidth = 60
            style = "-fx-background-color: grey;"
            minHeight = 60
        }
        button
    
    def createMap(mapWidth: Double, mapHeight: Double, numColumns: Int, numRows: Int): Seq[Seq[Rectangle]] = {
        val cellWidth = squareSide
        val cellHeight = squareSide

        (0 until numRows).map { row =>
            (0 until numColumns).map { col =>
            val rect = new Rectangle {
                fill = if ((row + col) % 2 == 0) Color.LightGrey else Color.Grey
                stroke = Color.Black
                strokeWidth = 1
                width = squareSide
                height = squareSide
            }
            rect.translateX = col * cellWidth
            rect.translateY = row * cellHeight
            rect
            }
        }
    }
}