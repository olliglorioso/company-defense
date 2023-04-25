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
import javafx.scene.paint.ImagePattern
import scalafx.scene.image.Image

abstract class Tower(path: String, price: Int, val range: Int, showUpgradeInfo: Tower => Unit, showMessage: (String, String, Int) => Unit)
    extends GameObject(path, TOWER_SIDE):
    val boundBox = 0.0
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

    onMouseClicked = (e: MouseEvent) => showUpgradeInfo(this)
    

    private def enemyPriorityCalc(enemy: Enemy): Double =
        val distToEnemy = enemy.getDistanceToPoint(getGlobalCenter)
        val generalPrio = enemy.health * 0.4 + enemy.speed * 0.01 + -distToEnemy * 0.25 + enemy.tilesTraversed * (if distToEnemy <= range then 10000 else 0)
        if (distToEnemy > range) (generalPrio - 1000)
        else generalPrio
    end enemyPriorityCalc

    def sellPrice: Int =
        math.round((price * 0.5) * math.pow(1.2, level.value)).toInt
    end sellPrice

    def upgradePrice: Int =
        math.round((price * 0.6) * math.pow(1.3, level.value)).toInt
    end upgradePrice

    private def upgradeFeatures(): Unit =
        level.value += 1
        attackSpeed *= 0.97
        damage *= 1.05
        slowDown *= 1.05
    end upgradeFeatures

    def upgrade(money: Double): Double =
        if (money < upgradePrice) then
            showMessage("You don't have enough money to upgrade this tower", "error", 1)
            money
        else if (level.value < maxLevel) then
            val moneyLeft = money - upgradePrice
            upgradeFeatures()
            moneyLeft
        else 
            showMessage("You can't upgrade this tower any further", "error", 1)
            money
    end upgrade

    def addEnemyToPriorityQueue(enemy: Enemy) =
        enemyPriority.enqueue(enemy)
    end addEnemyToPriorityQueue

    def clearPriorityQueue() =
        enemyPriority.clear()
    end clearPriorityQueue

    private def canShootTowardsEnemy(enemy: Enemy): Boolean =
        if (enemy.isInstanceOf[CamouflagedEnemy]) then return false
        val towerLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
        val distToEnemy = enemy.getDistanceToPoint(towerLoc)
        if (distToEnemy <= range) then true
        else false
    end canShootTowardsEnemy

    private def canInitNewBullet(time: Long, lastBulletInit: Long): Boolean =
        (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head) && (lastBulletInit == 0L || time - lastBulletInit >= attackSpeed * 100000000L))
    end canInitNewBullet


    def initBullet(time: Long): Bullet = 
        if (canInitNewBullet(time, lastBulletInit)) then 
            val closestEnemy = enemyPriority.head
            val enemyLoc = closestEnemy.getGlobalCenter
            val bullet = Bullet(bulletLoc, enemyLoc, bulletSpeed, slowDown, damage, closestEnemy, boundBox)
            bullet.x.value = getGlobalCenter.x
            bullet.y.value = getGlobalCenter.y
            bullet.rotate = rotate.value - 30 + 180
            lastBulletInit = time
            bullet
        else null
    end initBullet

    def rotateTowardsPriorityEnemy() =
        if (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head)) then
            val enemy = enemyPriority.head
            val angle = math.atan2(enemy.getGlobalCenter.y - getGlobalCenter.y, enemy.getGlobalCenter.x - getGlobalCenter.x)
            rotate.value = math.toDegrees(angle) + 90 // Make the head towards the enemy (shooting happens from the "head" of tower)
    end rotateTowardsPriorityEnemy
end Tower