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
import scalafx.scene.image._
import scalafx.scene.image.ImageView
import scalafx.beans.property.ObjectProperty
import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import Util.Constants._
import Logic.{BasicEnemy => BasicEnemyClass, CamouflagedEnemy => CamouflagedEnemyClass, SplittingEnemy => SplittingEnemyClass}
import Logic._
import scalafx.animation.AnimationTimer
import java.util.concurrent.TimeUnit
import scala.collection.mutable.Queue
import Util.FileHandler
import scala.collection.mutable.Buffer
import Util.Constants._
import scalafx.scene.control.Tooltip
import scalafx.beans.property._

sealed abstract class EnemyType(val value: Int)
case object BasicEnemy extends EnemyType(1)
case object SplittingEnemy extends EnemyType(2)
case object TankEnemy extends EnemyType(3)
case object FlockEnemy extends EnemyType(4)
case object CamouflagedEnemy extends EnemyType(5)

/** Responsible for the main gameplay scene.
  *
  * @param w
  *   Scene width
  * @param h
  *   Scene height
  */
class GameplayUI(w: Double, h: Double) extends Scene(w, h) {
  val mapInst: GameMap = new GameMap("test_map.txt")
  val waves: Array[Queue[EnemyType]] = generateWaves("test_wavedata.txt")
  val map = createMap(UI_TILE_SIZE, mapInst.map)
  var enemiesOnMap = Buffer[Enemy]()
  var towersOnMap = BufferProperty[Tower](Seq())
  var bulletsOnMap = BufferProperty[Bullet](Seq())
  val variates = ObjectProperty(
    Map("money" -> 500.0, "health" -> 10.0, "waveNo" -> 0.0, "score" -> 0.0)
  )
  var timerStarted = false
  var startTime = 0L
  var lastTime = 0L

  var pane = new Pane {
    children = map.flatten
    prefWidth = (w - SIDEBAR_WIDTH)
    prefHeight = h
  }

  val sidebar = SidebarUI(pane, mapInst, variates, towersOnMap, showMessage)
  sidebar.toFront()

  root = new BorderPane {
    center = new BorderPane { // Map area
      center = pane
    }
    right = sidebar // Sidebar area
  }


  def editPriorityQueuesAndCreateBullets(time: Long) = {
    for (tower <- towersOnMap.value) {
      tower.clearPriorityQueue()  // Clear the whole priority queue every time (have to remove and reinsert enemies anyway)
      for (enemy <- enemiesOnMap) {
        tower.addEnemyToPriorityQueue(enemy)
        tower.rotateTowardsPriorityEnemy()
      }
      val bullet: Bullet = tower.initBullet(time)
      if (bullet != null) then 
        bullet.toFront()
        pane.children.add(bullet)
        bulletsOnMap.value = bulletsOnMap.value :+ bullet
    }
  }

  def moveBulletsAndCheckHits(time: Long) = {
    val enemiesOnMapCopy = enemiesOnMap.clone()
    val bulletsOnMapCopy = bulletsOnMap.value.clone()
    for (bullet <- bulletsOnMapCopy) {
      bullet.move()
      if (bullet.isOnTarget()) {
        pane.children.remove(bullet)
        bulletsOnMap.value = bulletsOnMap.value.filter(_ != bullet)
      }
      for (enemy <- enemiesOnMapCopy) {
        if (enemy.getDistanceToPoint(bullet.getGlobalCenter) <= enemy.boundBox) {
          enemy.getHit(bullet.damage, bullet.slowDown)
          if (enemy.health <= 0 && enemy.isInstanceOf[SplittingEnemyClass]) {
            variates.value = variates.value.updated("money", variates.value("money") + enemy.moneyReward)
            variates.value = variates.value.updated("score", variates.value("score") + math.round(enemy.moneyReward/2))
            val newEnemies: Seq[BasicEnemy] = enemy.asInstanceOf[SplittingEnemyClass].split()
            for (newEnemy <- newEnemies) {
              pane.children.add(newEnemy)
              enemiesOnMap += newEnemy
            }
            pane.children.remove(enemy)
            enemiesOnMap = enemiesOnMap.filter(_ != enemy)
          } else if (enemy.health <= 0) {
            variates.value = variates.value.updated("money", variates.value("money") + enemy.moneyReward)
            variates.value = variates.value.updated("score", variates.value("score") + math.round(enemy.moneyReward/2))
            pane.children.remove(enemy)
            enemiesOnMap = enemiesOnMap.filter(_ != enemy)
          }
        }
      }
    }
  }

  /** Creates a basic clock for the game. Started when current gameplay starts.
    *
    * @return
    *   AnimationTimer
    */
  def createTimer(): AnimationTimer = {
    AnimationTimer { time =>
      {
        if (timerStarted == false) {
          showMessage("Wave " + ((variates.value("waveNo") + 1).toInt.toString()), "info", 5)
          startTime = time
          timerStarted = true
          lastTime = time
        } else {
          if (time - lastTime >= 1000000000L) {
            val currWave = waves(variates.value("waveNo").toInt)
            if (currWave.length < 1) {
              if (enemiesOnMap.length < 1)
                // wait for five seconds
                variates.value = variates.value.updated("waveNo", variates.value("waveNo") + 1)
                showMessage("Wave " + ((variates.value("waveNo") + 1).toInt.toString()), "info", 5)
                
            } else {
              val newEnemyType = currWave.dequeue
              val enemy = spawnEnemy(newEnemyType)
              lastTime = time
              pane.children.add(enemy)
              enemiesOnMap += enemy
            }
          }
          val newEnemies = Buffer[Enemy]()
          for (enemy <- enemiesOnMap) {
            enemy.move(newEnemies, pane, variates)
          }
          enemiesOnMap = newEnemies
          editPriorityQueuesAndCreateBullets(time)
          moveBulletsAndCheckHits(time)
        }
      }
    }
  }

  val timer = createTimer()

  /** Creates a blinking label to the middle of the screen for two seconds.
    * @param text
    *   Text to be displayed
    * @param color
    *   Color of the text
    * @param blinks
    *  Number of blinks
    */
  def showMessage(msg: String, messageType: String, blinks: Int): Unit = {
    val label = new Label(msg) {
      messageType match {
        case "error" =>
          style =
            "-fx-font-size: 50pt; -fx-text-fill: red;;"
        case "info" =>
          style =
            "-fx-font-size: 50pt; -fx-text-fill: white; -fx-background-color: black; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"
      }
      // get the length of the text related to the screen
      val textLength = msg.length * 50
      val newLoc = messageType match {
        case "error" =>
          ((w - SIDEBAR_WIDTH) / 2 - textLength / 3, 0.0)
        case "info" =>
          ((w - SIDEBAR_WIDTH) / 2 - textLength / 3, h / 2)
      }
      translateX = newLoc._1
      translateY = newLoc._2
      opacity = 1.0
    }
    label.toFront()
    pane.children.add(label)
    var timerStarted = false
    var startTime = 0L
    var timer: AnimationTimer = null
    var count = 0
    timer = AnimationTimer { time =>
      {
        if (timerStarted == false) {
          startTime = time
          timerStarted = true
        } else {
          if (time - startTime >= 500000000L) {
            label.opacity = 1.0 - label.opacity.value
            count += 1
            startTime = time
          }
          if (count > blinks) {
            pane.children.remove(label)
            timer.stop()
          }
        }
      }
    }
    timer.start()
  }

  /** Generate enemy waves Array based on a file.
    *
    * @param fileLoc
    *   File location
    * @return
    */
  private def generateWaves(fileLoc: String): Array[Queue[EnemyType]] = {
    val lines = FileHandler().readLinesFromFile("/WaveData/test_wavedata.txt")
    var helperArray: Array[Queue[EnemyType]] = Array()
    for (i <- lines) {
      val stringArray = i.split("")
      var helperQueue: Queue[EnemyType] = Queue()
      for (str <- stringArray) {
        val enemy = str match
          case "B"     => BasicEnemy
          case "C"     => CamouflagedEnemy
          case "S"     => SplittingEnemy
          case "T"     => TankEnemy
          case "F"     => FlockEnemy
          case default => BasicEnemy
        helperQueue.enqueue(enemy)
      }
      helperArray = helperArray :+ helperQueue
    }
    helperArray
  }

  /** Spawn an enemy to the start point.
    *
    * @return
    *   Enemy
    */
  def spawnEnemy(enemyType: EnemyType): Enemy = {
    val enemy = enemyType match
      case BasicEnemy => BasicEnemyClass(mapInst.pathQueue)
      case SplittingEnemy => SplittingEnemyClass(mapInst.pathQueue)
      case CamouflagedEnemy => CamouflagedEnemyClass(mapInst.pathQueue)
      case FlockEnemy => BasicEnemyClass(mapInst.pathQueue)
      case TankEnemy => BasicEnemyClass(mapInst.pathQueue)
    val startY =
      if (mapInst.startPoint.coord._1 == 0) then -1
      else mapInst.startPoint.coord._1
    val startX =
      if (mapInst.startPoint.coord._2 == 0) then -1
      else mapInst.startPoint.coord._2
    enemy.translateY = startY * UI_TILE_SIZE
    enemy.translateX = startX * UI_TILE_SIZE
    enemy
  }

  /** Create map UI based on the Map-class's map.
    *
    * @param UI_TILE_SIZE
    * @param mapTiles
    * @return
    */
  def createMap(
      UI_TILE_SIZE: Double,
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
          fill = if (tile.isInstanceOf[BgTile]) Color.Green else Color.Brown
          width = UI_TILE_SIZE
          height = UI_TILE_SIZE
        }
        rect.translateX = xPlus * UI_TILE_SIZE
        rect.translateY = yPlus * UI_TILE_SIZE
        rowBlocks = rowBlocks :+ rect
        xPlus = xPlus + 1
      }
      yPlus = yPlus + 1
      mapBlocks = mapBlocks :+ rowBlocks
    }
    mapBlocks
  }
}
