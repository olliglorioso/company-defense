package Logic
import Logic._
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.scene.input.InputIncludes.jfxMouseEvent2sfx
import scala.collection.mutable.PriorityQueue
import Util.Constants._

abstract class Tower(path: String, price: Int, range: Int)
    extends GameObject(path, TOWER_SIDE) {

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

    def initBullet(time: Long): Bullet

    def rotateTowardsPriorityEnemy() = {
        if (enemyPriority.nonEmpty) {
            val enemy = enemyPriority.head
            val angle = math.atan2(enemy.translateY.value - y.value, enemy.translateX.value - x.value)
            rotate.value = math.toDegrees(angle) + 90.0 // Make the head towards the enemy (shooting happens from the "head" of tower)
        }
    }

}