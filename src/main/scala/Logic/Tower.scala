package Logic
import Logic._
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.scene.input.InputIncludes.jfxMouseEvent2sfx
import scala.collection.mutable.PriorityQueue
import Util.Constants._
import scalafx.geometry.Point2D

abstract class Tower(path: String, price: Int, range: Int)
    extends GameObject(path, TOWER_SIDE) {
    
    val attackSpeed = 1000000000L

    def enemyPriorityCalc(enemy: Enemy): Double = {
        val distToEnemy = enemy.getDistanceToPoint(x.value, y.value)
        val generalPrio = enemy.health * 0.2 + enemy.speed * 0.1 + -distToEnemy * 0.7
        if (distToEnemy > range) (generalPrio - 1000)
        else generalPrio
    }
    
    // Enemy priority queue
    val enemyPriority = new PriorityQueue[Enemy]()(Ordering.by(enemyPriorityCalc(_)))

    def addEnemyToPriorityQueue(enemy: Enemy) = {
        enemyPriority.enqueue(enemy)
    }   

    def clearPriorityQueue() = {
        enemyPriority.clear()
    }

    def canShootTowardsEnemy(enemy: Enemy): Boolean = {
        val towerLoc = localToScene(x.value, y.value)
        val distToEnemy = enemy.getDistanceToPoint(towerLoc.x, towerLoc.y)
        if (distToEnemy <= range) true
        else false
    }

    def createNewBullet(enemyLoc: Point2D): Bullet = {
        val bulletLoc = path match {
            case SLOW_DOWN_TOWER_LOC => SLOW_DOWN_BULLET_LOC
            case REGULAR_TOWER_LOC => REGULAR_BULLET_LOC
        }
        Bullet(bulletLoc, (enemyLoc.x - (TOWER_SIDE), enemyLoc.y - (TOWER_SIDE)), 10, 0, 1)
    }

    def initBullet(time: Long): Bullet = {
        var lastInit = 0L
        if (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head) && (lastInit == 0 || time - lastInit >= attackSpeed)) then 
            val closestEnemy = enemyPriority.head
            val towerLoc = localToScene(x.value, y.value)
            val enemyLoc = closestEnemy.localToScene(closestEnemy.x.value, closestEnemy.y.value)

            val bullet = createNewBullet(enemyLoc)
            bullet.x.value = towerLoc.x
            bullet.y.value = towerLoc.y
            lastInit = time
            bullet
        else null
    }

    def rotateTowardsPriorityEnemy() = {
        if (enemyPriority.nonEmpty) {
            val enemy = enemyPriority.head
            val angle = math.atan2(enemy.translateY.value - y.value, enemy.translateX.value - x.value)
            rotate.value = math.toDegrees(angle) + 90.0 // Make the head towards the enemy (shooting happens from the "head" of tower)
        }
    }

}