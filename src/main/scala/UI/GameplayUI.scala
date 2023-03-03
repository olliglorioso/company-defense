package UI

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
import Util.UIConstants
import Logic.*
import scalafx.animation.AnimationTimer
import java.util.concurrent.TimeUnit

class GameplayUI (stage: JFXApp3.PrimaryStage, w: Double, h: Double) {
  val SIDEBAR_WIDTH = 130
  val MAP_WIDTH = 20
  val MAP_HEIGHT = 12
  val squareSide = (w - SIDEBAR_WIDTH) / MAP_WIDTH
  val mapInst: GameMap = new GameMap("test_map.txt")

  /** @param stage
    *   PrimaryStage
    * @param w
    *   Width of the scene
    * @param h
    *   Height of the scene
    * @return
    *   Gameplay-scene
    */
  def gameplayScene(): Scene =
    
    val enemy = spawnEnemy((100, 100))
    val map = createMap(squareSide, mapInst.map)

    /* lazy val gameLoop = AnimationTimer { time =>
      val seconds = TimeUnit.MILLISECONDS.toSeconds(time);
      moveEnemy(enemy)
    }
    gameLoop.start() */

    val gameplayScene: Scene = new Scene(w, h) {
      root = new BorderPane {
        right = sidebar()
        center = new BorderPane {
          center = new Pane {
            children = map.flatten :+ enemy
            prefWidth = (w - SIDEBAR_WIDTH)
            prefHeight = h
          }
        }
      }
    }

    gameplayScene



  def sidebar(): VBox =
    val regularTower = UIConstants.towerButton("file:src/resources/RegularTower.png")
    val slowDownTower = UIConstants.towerButton("file:src/resources/SlowDownTower.png")

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

  

  def spawnEnemy(coord: Tuple2[Int, Int]): Enemy = {
    val enemy = Enemy("file:src/resources/BasicEnemy.jpg", 80, "IBM", 3)
    println(mapInst.startPoint)
    enemy.translateX = (mapInst.startPoint._1 + 1) * squareSide
    enemy.translateY = (mapInst.startPoint._2 + 1) * squareSide
    
    enemy
  }

  def moveEnemy(enemy: Enemy): Unit = {
    enemy.translateX.value += enemy.speed1
  }

  def createMap(
      squareSide: Double,
      mapTiles: Array[Array[Tile]]
  ): Seq[Seq[Rectangle]] = {
    var mapBlocks: Seq[Seq[Rectangle]] = Seq()
    var yPlus = 0
    for (row <- mapTiles) {
      var rowBlocks: Seq[Rectangle] = Seq()
      var xPlus = 0
      for (tile <- row) {
        val rect = new Rectangle {
          // check if tile is BgTile-class
          fill =
            if (tile.isInstanceOf[BgTile]) Color.Green else Color.Brown
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
    mapBlocks
  }
}
