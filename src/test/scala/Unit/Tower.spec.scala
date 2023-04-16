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
import UI._



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
class TowerTest extends AnyFlatSpec with Matchers {
  
  var gui: Option[MainMenuUI] = None

  @Start
  def start(stage: Stage): Unit =
    val newGui = MainMenuUI(null, null)
    stage.setScene(newGui)
    gui = Some(newGui)
    stage.show()
  
  "App" should "initialize with default values" in {
    val tower = new TowerExtended()
    tower.attackSpeed should be (3.0)
    tower.lastBulletInit should be (0L)
    tower.damage should be (1.0)
    tower.slowDown should be (1.0)
    tower.level.value should be (1)
  }

  "App" should "calculate sell price correctly" in {
    val tower = new TowerExtended()
    tower.sellPrice should be (60)
  }

  "App" should "calculate upgrade price correctly" in {
    val tower = new TowerExtended()
    tower.upgradePrice should be (78)
  }

  "App" should "upgrade features correctly" in {
    val tower = new TowerExtended()
    tower.upgrade(100)
    tower.attackSpeed should be (2.91 +- 0.01)
    tower.damage should be (1.05)
    tower.slowDown should be (1.05)
    tower.level.value should be (2)
  }

  "App" should "upgrade tower and return remaining money" in {
    val tower = new TowerExtended()
    val remainingMoney = tower.upgrade(100.0)
    tower.level.value should be (2)
    remainingMoney should be (22.0)
  }

  "App" should "not upgrade tower if money is insufficient" in {
    val tower = new TowerExtended()
    val remainingMoney = tower.upgrade(50.0)
    tower.level.value should be (1)
    remainingMoney should be (50)
  }

  "App" should "not upgrade tower if already at max level" in {
    val tower = new TowerExtended()
    tower.upgrade(1000)
    tower.upgrade(1000)
    tower.upgrade(1000)
    tower.upgrade(1000)
    tower.level.value should be (5)
    val remainingMoney = tower.upgrade(100)
    tower.level.value should be (5)
    remainingMoney should be (100)
  }

  "App" should "work correctly with priorityqueue" in {
    val enemy = new BasicEnemy(Queue(PathTile((1,1), NoTurn), PathTile((1,1), NoTurn)))
    val tower = new TowerExtended()
    tower.addEnemyToPriorityQueue(enemy)
    tower.enemyPriority.head should be (enemy)
    tower.clearPriorityQueue()
    tower.enemyPriority.isEmpty should be (true)
  }

  "Tower" should "return correct global coordinates and be able to shoot enemy in the same pos" in {
    val tower = new TowerExtended()
    val enemy = new BasicEnemy(Queue(PathTile((1,1), NoTurn), PathTile((1,1), NoTurn)))
    tower.getGlobalCenter.x should be (TOWER_SIDE / 2)
    tower.getGlobalCenter.y should be (TOWER_SIDE / 2)
    tower.canShootTowardsEnemy(enemy) should be (true)
  }
}
