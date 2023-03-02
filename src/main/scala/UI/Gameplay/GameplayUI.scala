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
  val SIDEBAR_WIDTH = 150
  val MAP_WIDTH = 20
  val MAP_HEIGHT = 12

  /** @param stage
    *   PrimaryStage
    * @param w
    *   Width of the scene
    * @param h
    *   Height of the scene
    * @return
    *   Gameplay-scene
    */
  def gameplayScene(stage: JFXApp3.PrimaryStage, w: Double, h: Double): Scene =
    val squareSide = (w - SIDEBAR_WIDTH) / MAP_WIDTH
    val mapBlocks = new Logic.GameMap("test_map.txt").map
    val map = createMap(squareSide, mapBlocks)

    val gameplayScene: Scene = new Scene(w, h) {
      root = new BorderPane {
        right = sidebar()
        center = new BorderPane {
          center = new Pane {
            children = map.flatten
            prefWidth = (w - SIDEBAR_WIDTH)
            prefHeight = h
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
        regularTower,
        slowDownTower
      )
      // set sidebar width
      prefWidth = SIDEBAR_WIDTH
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

  def createMap(
      squareSide: Double,
      mapTiles: Array[Array[Logic.Tile]]
  ): Seq[Seq[Rectangle]] = {
    var mapBlocks: Seq[Seq[Rectangle]] = Seq()
    var yPlus = 0
    for (row <- mapTiles) {
      var rowBlocks: Seq[Rectangle] = Seq()
      var xPlus = 0
      for (tile <- row) {
        val rect = new Rectangle {
          // check if tile is Logic.BgTile-class
          fill =
            if (tile.isInstanceOf[Logic.BgTile]) Color.Green else Color.Brown
          width = squareSide
          height = squareSide
        }
        rect.translateX = xPlus * squareSide
        rect.translateY = yPlus * squareSide
        rowBlocks = rowBlocks :+ rect
        xPlus = xPlus + 1
      }
      yPlus = yPlus + 1
      mapBlocks = mapBlocks :+ rowBlocks
    }
    /* for (i <- mapBlocks) {
            for (b <- mapBlocks) {
                println(b)
            }
        } */
    mapBlocks
  }
}
