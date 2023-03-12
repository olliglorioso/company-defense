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
import scala.collection.mutable.Queue
import Util.FileHandler
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import scala.collection.mutable.Buffer

sealed abstract class EnemyType(val value: Int)
case object BasicEnemy extends EnemyType(1)
case object SplittingEnemy extends EnemyType(2)
case object TankEnemy extends EnemyType(3)
case object FlockEnemy extends EnemyType(4)
case object CamouflagedEnemy extends EnemyType(5)

class GameplayUI (w: Double, h: Double) extends Scene (w, h) {
  val SIDEBAR_WIDTH = 130
  val MAP_WIDTH = 20
  val MAP_HEIGHT = 12
  val squareSide = (w - SIDEBAR_WIDTH) / MAP_WIDTH // 89.5
  val mapInst: GameMap = new GameMap("test_map.txt")
  val waves: Array[Queue[EnemyType]] = generateWaves("test_wavedata.txt")
  var gameHp = 10
  val map = createMap(squareSide, mapInst.map)
  val enemiesOnMap = Buffer[Enemy]()

  var waveNo = 0
  var timerStarted = false
  var startTime = 0L
  var lastTime = 0L

  var pane = new Pane {
    children = map.flatten
    prefWidth = (w - SIDEBAR_WIDTH)
    prefHeight = h
  }

  root = new BorderPane {
    right = sidebar()
    center = new BorderPane {
      center = pane
    }
  }

  val timer = AnimationTimer { time => {
    if (timerStarted == false) {
      startTime = time
      timerStarted = true
      lastTime = time
    } else {
      if (time - lastTime >= 1000000000L) {
        val currWave = waves(waveNo)
        val newEnemyType = currWave.dequeue
        val enemy = spawnEnemy(newEnemyType)
        lastTime = time
        pane.children.add(enemy)
        enemiesOnMap += enemy
      }
      for (a <- enemiesOnMap) {
        moveEnemy(a)
      }
    }
  }}

  private def generateWaves(fileLoc: String): Array[Queue[EnemyType]] = {
    val lines = FileHandler().readLinesFromFile("/WaveData/test_wavedata.txt")
    var helperArray: Array[Queue[EnemyType]] = Array()
    for (i <- lines) {
      val stringArray = i.split("")
      var helperQueue: Queue[EnemyType] = Queue()
      for (str <- stringArray) {
        val enemy = str match
          case "B" => BasicEnemy
          case "C" => CamouflagedEnemy
          case "S" => SplittingEnemy
          case "T" => TankEnemy
          case "F" => FlockEnemy
        helperQueue.enqueue(enemy)
      }
      helperArray = helperArray :+ helperQueue
    }
    helperArray
  }

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
  def spawnEnemy(enemyType: EnemyType): Enemy = {
    val enemy = enemyType match
      case BasicEnemy => Enemy(UIConstants.BasicEnemy, squareSide.toInt, "IBM", 2, mapInst.pathQueue)
      case SplittingEnemy => Enemy(UIConstants.SplittingEnemy, squareSide.toInt, "Google", 5, mapInst.pathQueue)
      case CamouflagedEnemy => Enemy(UIConstants.CamouflagedEnemy, squareSide.toInt, "TSMC", 3, mapInst.pathQueue)
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
      val angle = (math.atan2(dy, dx) * 180 / math.Pi).round.toInt
      val roundedAngle = (Math.round(angle / 10.0) * 10).toInt
      enemy.rotate = roundedAngle
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
