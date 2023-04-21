

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import collection.mutable.Buffer
import Util.Constants._
import scala.collection.immutable.Queue
import _root_.Logic._

import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import org.testfx.framework.junit5.Stop
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.testfx.api.FxRobot
import org.testfx.api.FxAssert
import org.testfx.matcher.control.LabeledMatchers
import javafx.scene.control.Button
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._
import javafx.scene.Node
import UI.MainMenuUI
import javafx.application.Platform
import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite
import UI.GameplayUI
import scalafx.scene.layout.Pane
import UI.MainUI
import scalafx.scene.Scene
import UI.SettingsUI
import scalafx.scene.layout.BorderPane

case class TowerExtended() extends Tower(SLOW_DOWN_TOWER_LOC, 100, 50, (_: Tower) => {}, (_: String, _: String, _: Int) => {}):
  slowDown = 1
  def canShootTowardsEnemy(enemy: Enemy): Boolean =
      if (enemy.isInstanceOf[CamouflagedEnemy]) then return false
      val towerLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
      val distToEnemy = enemy.getDistanceToPoint(towerLoc)
      if (distToEnemy <= 50) then true
      else false
  end canShootTowardsEnemy
end TowerExtended

@ExtendWith(Array(classOf[ApplicationExtension]))
class GUITests:

  var gui: Option[MainMenuUI] = None
  val validMap = new GameMap("/Maps/test.valid_map.txt")
  var stageForTests: Stage = null
  var enemy: Enemy = null
  var pane = new Pane:
  end pane
  
  @Start
  def start(stage: Stage): Unit =
      def setSceneTo(sc: Scene) =
        stage.setScene(sc)
      val newGui = MainMenuUI(setSceneTo, null)
      stage.setScene(newGui)
      gui = Some(newGui)
      enemy = new BasicEnemy(validMap.pathQueue)
      stageForTests = stage
      stage.show()
  
  @Test
  def testTowerInit(): Unit =
    val tower = new TowerExtended()
    assert(tower.attackSpeed == 3.0)
    assert(tower.lastBulletInit == 0L)
    assert(tower.damage == 1.0)
    assert(tower.slowDown == 1.0)
    assert(tower.level.value == 1)
  end testTowerInit

  @Test
  def testTowerSellPrice(): Unit =
    val tower = new TowerExtended()
    assert(tower.sellPrice == 60)
  end testTowerSellPrice
  
  @Test
  def testTowerUpgradePrice(): Unit =
    val tower = new TowerExtended()
    assert(tower.upgradePrice == 78)
  end testTowerUpgradePrice

  @Test
  def testTowerUpgradeFeatures(): Unit =
    val tower = new TowerExtended()
    tower.upgrade(100)
    assert(tower.attackSpeed == 2.91)
    assert(tower.damage == 1.05)
    assert(tower.slowDown == 1.05)
    assert(tower.level.value == 2)
  end testTowerUpgradeFeatures

  @Test
  def testTowerUpgradeReturn(): Unit =
    val tower = new TowerExtended()
    val remainingMoney = tower.upgrade(100)
    assert(tower.level.value == 2)
    assert(remainingMoney == 22)
  end testTowerUpgradeReturn

  @Test
  def testTowerUpgradeMaxLevel(): Unit =
    val tower = new TowerExtended()
    tower.upgrade(1000)
    tower.upgrade(1000)
    tower.upgrade(1000)
    tower.upgrade(1000)
    assert(tower.level.value == 5)
    val remainingMoney = tower.upgrade(100)
    assert(tower.level.value == 5)
    assert(remainingMoney == 100)
  end testTowerUpgradeMaxLevel

  @Test
  def testTowerUpgradeInsufficientMoney(): Unit =
    val tower = new TowerExtended()
    val remainingMoney = tower.upgrade(50)
    assert(tower.level.value == 1)
    assert(remainingMoney == 50)
  end testTowerUpgradeInsufficientMoney

  @Test
  def testTowerWithPriorityQueue(): Unit =
    val enemy = new BasicEnemy(Queue(PathTile((1,1), NoTurn), PathTile((1,1), NoTurn)))
    val tower = new TowerExtended()
    tower.addEnemyToPriorityQueue(enemy)
    assert(tower.enemyPriority.head == enemy)
    tower.clearPriorityQueue()
    assert(tower.enemyPriority.isEmpty)
  end testTowerWithPriorityQueue

  @Test
  def testTowerGlobalCoordinates(): Unit =
    val tower = new TowerExtended()
    val enemy = new BasicEnemy(Queue(PathTile((1,1), NoTurn), PathTile((1,1), NoTurn)))
    assert(tower.getGlobalCenter.x == TOWER_SIDE / 2)
    assert(tower.getGlobalCenter.y == TOWER_SIDE / 2)
    assert(tower.canShootTowardsEnemy(enemy))
  end testTowerGlobalCoordinates

  // ENEMY TESTS

  @Test
  def testEnemyInit(): Unit =
    assert(enemy.health == BASIC_ENEMY_HEALTH)
    assert(enemy.speed == BASIC_ENEMY_SPEED)
    assert(enemy.pathQueue == validMap.pathQueue)
    assert(enemy.getGlobalCenter.x == ENEMY_SIZE / 2)
    assert(enemy.getGlobalCenter.y == ENEMY_SIZE / 2)
  
  @Test
  def testEnemyGetHit(): Unit =
    val origHealth = enemy.health
    for i <- 1 to 9 do
      enemy.getHit(10, 0)
      assert(enemy.health == (origHealth - 10 * i))
  end testEnemyGetHit

  @Test
  def testEnemyDistance(): Unit =
    val newEnemy = BasicEnemy(validMap.pathQueue)
    val values = (-100 to 100 by 10).toList
    for x <- values do
      for y <- values do
        newEnemy.translateX = x
        newEnemy.translateY = y
        assert(enemy.getDistanceToPoint(newEnemy.getGlobalCenter) == math.sqrt(x*x + y*y))
  end testEnemyDistance

  @Test
  def testEnemyMovement(): Unit =
    val startY =
      if (validMap.startPoint.coord._1 == 0) then -1
      else validMap.startPoint.coord._1
    val startX =
      if (validMap.startPoint.coord._2 == 0) then -1
      else validMap.startPoint.coord._2
    enemy.translateY = startY * UI_TILE_SIZE
    enemy.translateX = startX * UI_TILE_SIZE

    for i <- 1 to 20 do
      enemy.move(pane)
      assert(enemy.getGlobalCenter.x == startX * UI_TILE_SIZE + ENEMY_SIZE / 2)
      assert(enemy.getGlobalCenter.y == startY * UI_TILE_SIZE + ENEMY_SIZE / 2 + i * BASIC_ENEMY_SPEED)
  end testEnemyMovement

  // BULLET GUI

  @Test
  def testBullet(): Unit =
    val bullet = new Bullet(BOMB_BULLET_LOC, enemy.getGlobalCenter, 5, 1, 1, enemy, 0)
    for i <- 1 to 5000 do
      bullet.move()
    assert(bullet.getGlobalCenter.distance(enemy.getGlobalCenter) <= 50)

  // INPUT TESTS

  @Test
  def testButtonTexts(robot: FxRobot): Unit =
    gui match
      case Some(gui) =>
        assert(
          gui.settingsButton.text() == "Settings",
          "The button should be called 'Settings'"
        )
        assert(
          gui.startNewGameButton.text() == "Start new game",
          "The button should be called 'Start new game'"
        )
        assert(
          gui.continueSavedGameButton.text() == "Continue saved game",
          "The button should be called 'Continue saved game'"
        )
      case None =>
  end testButtonTexts

      
end GUITests