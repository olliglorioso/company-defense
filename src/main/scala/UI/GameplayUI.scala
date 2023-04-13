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
import scalafx.scene.control._
import scalafx.scene.image._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.shape._
import scalafx.scene.paint.Color
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
import scala.collection.mutable.ArrayBuffer
import scalafx.scene.control._
import scalafx.scene.control.Alert.AlertType
import scalafx.application.Platform
import scalafx.application.JFXApp3.PrimaryStage
import Util.State._
import Util.HelperFunctions._
import ujson.Obj

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
class GameplayUI(stage: PrimaryStage, mainmenuScene: => Scene) extends Scene(screenWidth(), screenHeight()):
  lazy val mainmenuSceneLazy = mainmenuScene
  val mapInst: GameMap = new GameMap(getMap)
  var waves: Array[Queue[EnemyType]] = generateWaves()
  val map = createMap(UI_TILE_SIZE, mapInst.map)
  var enemiesOnMap = Buffer[Enemy]()
  var towersOnMap = BufferProperty[Tower](Seq())
  var towersOnMapAfterWaveChange = BufferProperty[Tower](Seq())
  var variatesAfterWaveChange = ObjectProperty(variates.value)
  var bulletsOnMap = BufferProperty[Bullet](Seq())
  var timerStarted = false
  var startTime = 0L
  var lastTime = 0L
  var gameOver = false

  var pane = new Pane:
    children = map.flatten
    prefWidth = (screenWidth() - SIDEBAR_WIDTH)
    prefHeight = screenHeight()
  end pane

  val sidebar = SidebarUI(pane, mapInst, towersOnMap, showMessage, saveAndExit, exit)
  sidebar.toFront()

  root = new BorderPane:
    center = new BorderPane: // Map area
      center = pane
    right = sidebar // Sidebar area

  /**
   * Exit to main menu without saving.
   */
  def exit(): Unit =
    timer.stop()
    stage.setScene(mainmenuSceneLazy)
  end exit

  /**
   * Save current game (save e.g. level, last completed wave, money, health, placed towers and their locs & levels).
   */
  def saveAndExit(): Unit =
    val savedTowers = ArrayBuffer[Obj]()
    for (tower <- towersOnMapAfterWaveChange.value) {
      val towerType = tower match
        case _: RegularTower => "R"
        case _: SlowDownTower => "S"
        case _ => null
      val towerLevel = tower.level.value
      val towerLoc = tower.getGlobalCenter
      val towerObj = Obj(
        "type" -> towerType,
        "globalX" -> towerLoc.x,
        "globalY" -> towerLoc.y,
        "level" -> towerLevel
      )
      savedTowers += towerObj
    }
    val savedGame = ujson.Value(Obj(
      "health" -> variatesAfterWaveChange.value("health"),
      "money" -> variatesAfterWaveChange.value("money"),
      "difficulty" -> difficulty.value,
      "waveNo" -> (if (variatesAfterWaveChange.value("waveNo") > 0) then (variatesAfterWaveChange.value("waveNo")) else 0),
      "score" -> variatesAfterWaveChange.value("score"),
      "towers" -> savedTowers,
    ))
    timer.stop()
    FileHandler().writeLinesToJsonFile("Saved/LATEST_saved.json", savedGame) // Write the saved info to a file (and empty previous one)
    stage.setScene(mainmenuSceneLazy)
  end saveAndExit

  /**
   * Initialize saved towers, in case user wants to continue saved game.
   * @param savedTowers Array of saved towers, should be read from a file.
   */
  def initializeSavedGame(savedTowers: ArrayBuffer[Obj]) = 
    variatesAfterWaveChange.setValue(variates.value)
    for (savedTower <- savedTowers) {
      val towerType = savedTower.value("type").value.asInstanceOf[String]
      val x = savedTower.value("globalX").value.asInstanceOf[Double] - TOWER_SIDE / 2
      val y = savedTower.value("globalY").value.asInstanceOf[Double] - TOWER_SIDE / 2
      val level = savedTower.value("level").value.asInstanceOf[Double].toInt

      if (mapInst.isBgTile(y + TOWER_SIDE / 2, x + TOWER_SIDE / 2) && towerCanBePlaced(Point2D(x, y), towersOnMap)) then 
        val newTower = towerType match
          case "R" => new RegularTower(sidebar.openUpgradeMenu, showMessage)
          case "S" => new SlowDownTower(sidebar.openUpgradeMenu, showMessage)
          case _ => null
        newTower.x = x
        newTower.y = y
        newTower.level.setValue(level)
        pane.children.add(newTower)
        towersOnMap.setValue(towersOnMap.value :+ newTower)
        towersOnMapAfterWaveChange.setValue(towersOnMapAfterWaveChange.value :+ newTower)
    }
  end initializeSavedGame

  /**
   * Edit the priority queues of all the towers on the map and create bullets if possible for each tower.
   * @param time Current time in milliseconds
   */
  def editPriorityQueuesAndCreateBullets(time: Long) = 
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
  end editPriorityQueuesAndCreateBullets

  /**
   * Move all the bullets on the map and check if they hit any enemies, if yes -> cause damage.
   * @param time Current time in milliseconds
   */
  def moveBulletsAndCheckHits(): Unit = 
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
            variates.setValue(variates.value.updated("money", variates.value("money") + enemy.moneyReward))
            variates.setValue(variates.value.updated("score", variates.value("score") + math.round(enemy.moneyReward/2)))
            val newEnemies: Seq[BasicEnemy] = enemy.asInstanceOf[SplittingEnemyClass].split()
            for (newEnemy <- newEnemies) {
              pane.children.add(newEnemy)
              enemiesOnMap += newEnemy
            }
            pane.children.remove(enemy)
            enemiesOnMap = enemiesOnMap.filter(_ != enemy)
          } else if (enemy.health <= 0) {
            variates.setValue(variates.value.updated("money", variates.value("money") + enemy.moneyReward))
            variates.setValue(variates.value.updated("score", variates.value("score") + math.round(enemy.moneyReward/2)))
            pane.children.remove(enemy)
            enemiesOnMap = enemiesOnMap.filter(_ != enemy)
          }
        }
      }
    }
  end moveBulletsAndCheckHits

  /** Creates a basic clock for the game. Started when current gameplay starts.
    *
    * @return
    *   AnimationTimer
    */
  def createTimer(): AnimationTimer = 
    AnimationTimer { time =>
      {
        if (timerStarted == false) {
          showMessage("Wave " + ((variates.value("waveNo") + 1).toInt.toString()), "info", 5)
          startTime = time
          timerStarted = true
          lastTime = time
        } else {
          if (time - lastTime >= 1000000000L) {
            val waveNo = variates.value("waveNo").toInt
            if (waveNo >= waves.length) then
              val newEnemy = spawnEnemy(getRandomEnemyType)
              lastTime = time
            else
              val currWave = waves(variates.value("waveNo").toInt)
              if (currWave.length < 1) then
                if (enemiesOnMap.length < 1)
                  variates.setValue(variates.value.updated("waveNo", variates.value("waveNo") + 1))
                  // Give the player some money for completing the wave, in an sqrt function to make the money amount grow slowly
                  variates.setValue(variates.value.updated("money", variates.value("money") + math.round(25 * math.sqrt((variates.value("waveNo") + 1).toInt))))
                  towersOnMapAfterWaveChange.setValue(towersOnMap.value)
                  variatesAfterWaveChange.setValue(variates.value)
                  showMessage("Wave " + ((variates.value("waveNo") + 1).toInt.toString()), "info", 4)
              else 
                val newEnemyType = currWave.dequeue
                val enemy = spawnEnemy(newEnemyType)
                lastTime = time
          }
          val newEnemies = new ArrayBuffer[Enemy](enemiesOnMap.length - 1)
          for (enemy <- enemiesOnMap) {
            val newEnemyInstance = enemy.move(pane)
            if (variates.value("health") <= 0 && !gameOver) {
              gameOver = true
              showMessage("Game Over", "info", 20)
              new Dialog[String]() {
                initOwner(stage)
                resizable=false
                title = "Game Over"
                headerText = s"Your points: ${variates.value("score").toInt}"
                dialogPane().buttonTypes = Seq(ButtonType.OK)
                resultConverter = _ => ""
                width_=(300)
                onHidden = _ => {
                  timer.stop()
                  stage.setScene(mainmenuSceneLazy)
                }
                onShowing = _ => {
                  timer.stop()
                }
              }.show()
            }
            if (newEnemyInstance != null) {
              newEnemies += newEnemyInstance
            }
          }
          enemiesOnMap = newEnemies
          editPriorityQueuesAndCreateBullets(time)
          moveBulletsAndCheckHits()
        }
      }
    }
  end createTimer

  val timer = createTimer()

  /** Creates a blinking label to the middle of the screen for two seconds.
    * @param text
    *   Text to be displayed
    * @param color
    *   Color of the text
    * @param blinks
    *  Number of blinks
    */
  def showMessage(msg: String, messageType: String, blinks: Int): Unit = 
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
          ((screenWidth() - SIDEBAR_WIDTH) / 2 - textLength / 3, 0.0)
        case "info" =>
          ((screenHeight() - SIDEBAR_WIDTH) / 2 - textLength / 3, screenHeight() / 2)
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
          if (count > blinks) then
            pane.children.remove(label)
            timer.stop()
        }
      }
    }
    timer.start()
  end showMessage

  /** Generate enemy waves Array based on a file.
    *
    * @param fileLoc
    *   File location
    * @return
    */
  private def generateWaves(): Array[Queue[EnemyType]] = 
    try
      val lines = FileHandler().readLinesFromFile(getWaveData)
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
            case default => throw new Exception("Invalid enemy type in wave data file.")
          helperQueue.enqueue(enemy)
        }
        helperArray = helperArray :+ helperQueue
      }
      helperArray
    catch
      case e: Exception => throw new Exception(e.getMessage())
  end generateWaves

  /** Spawn an enemy to the start point.
    *
    * @return
    *   Enemy
    */
  private def spawnEnemy(enemyType: EnemyType): Enemy = 
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
    pane.children.add(enemy)
    enemiesOnMap += enemy
    enemy
  end spawnEnemy

  /** Create map UI based on the Map-class's map.
    *
    * @param UI_TILE_SIZE
    * @param mapTiles
    * @return
    */
  private def createMap(
      UI_TILE_SIZE: Double,
      mapTiles: Array[Array[Tile]]
  ): Seq[Seq[Rectangle]] = 
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
  end createMap
end GameplayUI
