package Logic
import Logic.*
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.scene.input.InputIncludes.jfxMouseEvent2sfx
import scala.collection.mutable.PriorityQueue

case class Tower(path: String, size: Int, name: String, price: Int, range: Int)
    extends GameObject(path, size, name) {

    def enemyPriorityCalc(enemy: Enemy): Double = enemy.health * 0.3 + enemy.speed * 0.1 + enemy.getDistanceToPoint(x.value, y.value) * 0.6
    
    // Enemy priority queue
    val enemyPriority = new PriorityQueue[Enemy]()(Ordering.by(enemyPriorityCalc(_)))

    def addEnemyToPriorityQueue(enemy: Enemy) = {
        enemyPriority.enqueue(enemy)
    }   

    def clearPriorityQueue() = {
        enemyPriority.clear()
    }

    def rotateTowardsPriorityEnemy() = {
        println(x.value.toString() + " " +  y.value.toString())
        if (enemyPriority.nonEmpty) {
            val enemy = enemyPriority.head
            val angle = math.atan2(enemy.translateY.value - y.value, enemy.translateX.value - x.value)
            val angleDegrees = math.toDegrees(angle)
            rotate.value = angleDegrees
        }
    }

}