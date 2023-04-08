package Logic
import Logic._
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.scene.input.InputIncludes.jfxMouseEvent2sfx
import scala.collection.mutable.PriorityQueue
import Util.Constants._
import scalafx.geometry.Point2D

abstract class Tower(path: String, price: Int, range: Int)
    extends GameObject(path, TOWER_SIDE) {
    
    val attackSpeed = 2 // is a good basic speed. Adjusting between 0.5-5 is ok
    var lastBulletInit = 0L
    val bulletLoc = REGULAR_BULLET_LOC
    val damage = 1
    val slowDown = 0
    val bulletSpeed = 20
    // Enemy priority queue
    val enemyPriority = new PriorityQueue[Enemy]()(Ordering.by(enemyPriorityCalc(_)))

    def enemyPriorityCalc(enemy: Enemy): Double = {
        val distToEnemy = enemy.getDistanceToPoint(x.value, y.value)
        val generalPrio = enemy.health * 0.2 + enemy.speed * 0.1 + -distToEnemy * 0.7
        if (distToEnemy > range) (generalPrio - 1000)
        else generalPrio
    }

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

    def canInitNewBullet(time: Long, lastBulletInit: Long): Boolean = {
        (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head) && (lastBulletInit == 0L || time - lastBulletInit >= attackSpeed * 100000000L))
    }


    def initBullet(time: Long): Bullet = {
        
        if (canInitNewBullet(time, lastBulletInit)) then 
            val closestEnemy = enemyPriority.head
            val enemyLoc = closestEnemy.localToScene(closestEnemy.x.value, closestEnemy.y.value)

            val bullet = Bullet(bulletLoc, (enemyLoc.x, enemyLoc.y), bulletSpeed, slowDown, damage)
            bullet.x.value = x.value + 0.25*TOWER_SIDE
            bullet.y.value = y.value + 0.25*TOWER_SIDE
            lastBulletInit = time
            bullet
        else null
    }

    def rotateTowardsPriorityEnemy() = {
        if (enemyPriority.nonEmpty) {
            val enemy = enemyPriority.head
            val angle = math.atan2(enemy.translateY.value - y.value, enemy.translateX.value - x.value)
            rotate.value = math.toDegrees(angle) + 90 // Make the head towards the enemy (shooting happens from the "head" of tower)
        }
    }

}