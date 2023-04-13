package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._
import scalafx.Includes._
import javafx.scene.paint.ImagePattern

class SplittingEnemy (pathQueue: Queue[PathTile]) extends Enemy(SPLITTING_ENEMY_LOC, ENEMY_SIZE, SPLITTING_ENEMY_SPEED, pathQueue, SPLITTING_ENEMY_HEALTH) {
  var imageChanged3 = false
  override val moneyReward: Int = 7

  override def getHit(damage: Double, slowDown: Double = 0) = {
        getHitFinal(damage, slowDown, SPLITTING_ENEMY_2_LOC, SPLITTING_ENEMY_3_LOC)
        if (health >= 0 && health <= (origHealth / 4) && !imageChanged3) {
            imageChanged3 = true
            fill = new ImagePattern(new Image(SPLITTING_ENEMY_4_LOC))
        }
    }

    def split (): Seq[BasicEnemy] =
        println(queue.length)
        val enemy1 = new BasicEnemy(queue)
        val enemy2 = new BasicEnemy(queue)
        val enemy3 = new BasicEnemy(queue)
        enemy1.translateX = getGlobalCenter.x - ENEMY_SIZE / 2
        enemy1.translateY = getGlobalCenter.y - ENEMY_SIZE / 2
        enemy2.translateX = getGlobalCenter.x 
        enemy2.translateY = getGlobalCenter.y
        enemy3.translateX = getGlobalCenter.x - ENEMY_SIZE / 4
        enemy3.translateY = getGlobalCenter.y - ENEMY_SIZE / 4
        Seq(enemy1, enemy2, enemy3)
}
