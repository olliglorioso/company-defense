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
    
    def canShootEnemyAtPos(x: Double, y: Double) = {

    }        
}