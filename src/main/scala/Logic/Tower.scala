package Logic
import Logic._
import scalafx.Includes._
import scalafx.scene.input.InputIncludes.jfxMouseEvent2sfx
import scala.collection.mutable.PriorityQueue
import Util.Constants._
import scalafx.geometry.Point2D
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import scalafx.scene.control.Button
import scalafx.beans.property.IntegerProperty

abstract class Tower(path: String, price: Int, range: Int, showUpgradeInfo: Tower => Unit, showMessage: (String, String, Int) => Unit)
    extends GameObject(path, TOWER_SIDE) {
    
    var attackSpeed = 3.0 // is a good basic speed. Adjusting between 0.5-5 is ok. The lower the better
    var lastBulletInit = 0L
    val bulletLoc = REGULAR_BULLET_LOC
    var damage = 1.0
    var slowDown = 0.0
    val bulletSpeed = 40
    var level = IntegerProperty(1)
    val maxLevel = 5 
    // Enemy priority queue
    val enemyPriority = new PriorityQueue[Enemy]()(Ordering.by(enemyPriorityCalc(_)))

    onMouseClicked = (e: MouseEvent) => {
        showUpgradeInfo(this)
    }

    private def enemyPriorityCalc(enemy: Enemy): Double = {
        val distToEnemy = enemy.getDistanceToPoint(getGlobalCenter)
        val generalPrio = enemy.health * 0.4 + enemy.speed * 0.01 + -distToEnemy * 0.25 + enemy.tilesTraversed * 0.70
        if (distToEnemy > range) (generalPrio - 1000)
        else generalPrio
    }

    def sellPrice: Int = {
        math.round((price * 0.5) * math.pow(1.2, level.value)).toInt
    }

    def upgradePrice: Int = {
        math.round((price * 0.6) * math.pow(1.3, level.value)).toInt
    }

    def upgradeFeatures(): Unit = {
        level.value += 1
        attackSpeed *= 0.97
        damage *= 1.05
        slowDown *= 1.05
    }

    def upgrade(money: Double): Double = {
        if (money < upgradePrice) {
            showMessage("You don't have enough money to upgrade this tower", "error", 1)
            return money
        } else if (level.value < maxLevel) {
            upgradeFeatures()
            money - upgradePrice
        } else {
            showMessage("You can't upgrade this tower any further", "error", 1)
            money
        }
    }

    def addEnemyToPriorityQueue(enemy: Enemy) = {
        enemyPriority.enqueue(enemy)
    }   

    def clearPriorityQueue() = {
        enemyPriority.clear()
    }

    private def canShootTowardsEnemy(enemy: Enemy): Boolean = {
        if (enemy.isInstanceOf[CamouflagedEnemy]) return false
        val towerLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
        val distToEnemy = enemy.getDistanceToPoint(towerLoc)
        if (distToEnemy <= range) true
        else false
    }

    private def canInitNewBullet(time: Long, lastBulletInit: Long): Boolean = {
        (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head) && (lastBulletInit == 0L || time - lastBulletInit >= attackSpeed * 100000000L))
    }

    def getGlobalCenter: Point2D = {
        val towerLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
        towerLoc
    }

    def initBullet(time: Long): Bullet = { 
        if (canInitNewBullet(time, lastBulletInit)) then 
            val closestEnemy = enemyPriority.head
            val enemyLoc = closestEnemy.getGlobalCenter
            val bullet = Bullet(bulletLoc, enemyLoc, bulletSpeed, slowDown, damage, closestEnemy)
            bullet.x.value = getGlobalCenter.x
            bullet.y.value = getGlobalCenter.y
            lastBulletInit = time
            bullet
        else null
    }

    def rotateTowardsPriorityEnemy() = {
        if (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head)) {
            val enemy = enemyPriority.head
            val angle = math.atan2(enemy.getGlobalCenter.y - getGlobalCenter.y, enemy.getGlobalCenter.x - getGlobalCenter.x)
            rotate.value = math.toDegrees(angle) + 90 // Make the head towards the enemy (shooting happens from the "head" of tower)
        }
    }

}