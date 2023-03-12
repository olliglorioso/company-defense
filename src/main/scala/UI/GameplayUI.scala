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
import scala.collection.immutable.Queue

sealed abstract class EnemyType(val value: Int)
case object BasicEnemy extends EnemyType(1)


class GameplayUI (stage: JFXApp3.PrimaryStage, w: Double, h: Double) {
  val SIDEBAR_WIDTH = 130
  val MAP_WIDTH = 20
  val MAP_HEIGHT = 12
  val squareSide = (w - SIDEBAR_WIDTH) / MAP_WIDTH // 89.5
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
    val enemy = spawnEnemy()
    val map = createMap(squareSide, mapInst.map)

    lazy val gameLoop = AnimationTimer { time =>
      val seconds = TimeUnit.MILLISECONDS.toSeconds(time);
      moveEnemy(enemy)
    }
    gameLoop.start()

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

  /**
    * Spawn an enemy to the start point.
    *
    * @return Enemy
    */
  def spawnEnemy(): Enemy = {
    val enemy = Enemy("file:src/resources/BasicEnemy.jpg", squareSide.toInt, "IBM", 10, mapInst.pathQueue)
    enemy.translateY = (mapInst.startPoint.coord._1) * squareSide
    enemy.translateX = (mapInst.startPoint.coord._2) * squareSide
    enemy
  }

  /**
    * Move given enemy in the map towards next tile in the path.
    *
    * @param enemy
    */
  def moveEnemy(enemy: Enemy): Unit = {
    val (currY, currX) = (enemy.translateY.value, enemy.translateX.value)
    val (tileY, tileX) = (enemy.previousTile.coord._1 * squareSide, enemy.previousTile.coord._2 * squareSide)
    val (nextTileY, nextTileX) = (enemy.nextTile.coord._1 * squareSide, enemy.nextTile.coord._2 * squareSide)
    val (vecY, vecX) = (currY - tileY, currX - tileX)
    val (dy, dx) = (nextTileY - currY, nextTileX - currX)
    
    val distance = math.sqrt(dx * dx + dy * dy)
    val (vx, vy) = if (distance > 0) (((dx/distance)*enemy.speed1).round.toInt, ((dy/distance)*enemy.speed1).round.toInt) else (0, 0)
    
    if (distance <= enemy.speed1) {
      enemy.getNextTile()
    } else {
      enemy.translateX.value += vx.toInt
      enemy.translateY.value += vy
    }
  }

  /**
    * Create map UI based on the Map-class's map.
    *
    * @param squareSide
    * @param mapTiles
    * @return
    */
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
