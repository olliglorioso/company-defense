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
import Util.Constants._
import Logic.*
import scalafx.animation.AnimationTimer
import java.util.concurrent.TimeUnit
import scala.collection.mutable.Queue
import Util.FileHandler
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import scala.collection.mutable.Buffer
import Util.Constants._

sealed abstract class EnemyType(val value: Int)
case object BasicEnemy extends EnemyType(1)
case object SplittingEnemy extends EnemyType(2)
case object TankEnemy extends EnemyType(3)
case object FlockEnemy extends EnemyType(4)
case object CamouflagedEnemy extends EnemyType(5)

class GameplayUI (w: Double, h: Double) extends Scene (w, h) {
  val squareSide = (w - SIDEBAR_WIDTH) / MAP_WIDTH // 89.5
  val mapInst: GameMap = new GameMap("test_map.txt")
  val waves: Array[Queue[EnemyType]] = generateWaves("test_wavedata.txt")
  val map = createMap(squareSide, mapInst.map)
  var enemiesOnMap = Buffer[Enemy]()

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
        if (currWave.length < 1) {
          if (enemiesOnMap.length < 1) waveNo += 1
        } else {
          val newEnemyType = currWave.dequeue
          val enemy = spawnEnemy(newEnemyType)
          lastTime = time
          pane.children.add(enemy)
          enemiesOnMap += enemy
        }
      }
      val newEnemies = Buffer[Enemy]()
      for (a <- enemiesOnMap) {
        moveEnemy(a, newEnemies)
      }
      enemiesOnMap = newEnemies
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
          case default => BasicEnemy
        helperQueue.enqueue(enemy)
      }
      helperArray = helperArray :+ helperQueue
    }
    helperArray
  }

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

  /**
    * Spawn an enemy to the start point.
    *
    * @return Enemy
    */
  def spawnEnemy(enemyType: EnemyType): Enemy = {
    val enemy = enemyType match
      case BasicEnemy => Enemy(BASIC_ENEMY_LOC, squareSide.toInt, "IBM", 2, mapInst.pathQueue)
      case SplittingEnemy => Enemy(SPLITTING_ENEMY_LOC, squareSide.toInt, "Google", 5, mapInst.pathQueue)
      case CamouflagedEnemy => Enemy(CAMOUFLAGED_ENEMY_LOC, squareSide.toInt, "TSMC", 3, mapInst.pathQueue)
      case FlockEnemy => Enemy(BASIC_ENEMY_LOC, squareSide.toInt, "TSMC", 3, mapInst.pathQueue)
      case TankEnemy => Enemy(BASIC_ENEMY_LOC, squareSide.toInt, "TSMC", 3, mapInst.pathQueue)
    val startY = if (mapInst.startPoint.coord._1 == 0) then -1 else mapInst.startPoint.coord._1
    val startX = if (mapInst.startPoint.coord._2 == 0) then -1 else mapInst.startPoint.coord._2
    enemy.translateY = startY * squareSide
    enemy.translateX = startX * squareSide
    enemy
  }

  /**
    * Move given enemy in the map towards next tile in the path.
    *
    * @param enemy
    */
  def moveEnemy(enemy: Enemy, newEnemies: Buffer[Enemy]): Unit = {
    val (currY, currX) = (enemy.translateY.value, enemy.translateX.value)
    val (tileY, tileX) = (enemy.previousTile.coord._1 * squareSide, enemy.previousTile.coord._2 * squareSide)
    val (nextTileY, nextTileX) = (enemy.nextTile.coord._1 * squareSide, enemy.nextTile.coord._2 * squareSide)
    val (vecY, vecX) = (currY - tileY, currX - tileX)
    val (dy, dx) = (nextTileY - currY, nextTileX - currX)
    
    val distance = math.sqrt(dx * dx + dy * dy)
    val (vx, vy) = if (distance > 0) (((dx/distance)*enemy.speed1).round.toInt, ((dy/distance)*enemy.speed1).round.toInt) else (0, 0)
    
    if (distance <= enemy.speed1) {
      if (enemy.nextTile.getTurn() == End) {
        pane.children.remove(enemy)
        return
      } else {
        enemy.getNextTile()
        newEnemies += enemy
      }
    } else {
      enemy.translateX.value += vx.toInt
      enemy.translateY.value += vy
      val angle = (math.atan2(dy, dx) * 180 / math.Pi).round.toInt
      val roundedAngle = (Math.round(angle / 10.0) * 10).toInt
      enemy.rotate = roundedAngle
      newEnemies += enemy
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
