package Unit

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import collection.mutable.Buffer
import Util.Constants._
import scala.collection.immutable.Queue
import _root_.Logic._

import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
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
class TowerTest {
  
  var gui: Option[MainMenuUI] = None

  @Start
  def start(stage: Stage): Unit =
    val newGui = MainMenuUI(null, null)
    stage.setScene(newGui)
    gui = Some(newGui)
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
}
