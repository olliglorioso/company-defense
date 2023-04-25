package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._
import scalafx.Includes._
import javafx.scene.paint.ImagePattern

class AppleEnemy (pathQueue: Queue[PathTile]) extends Enemy(APPLE_ENEMY_LOC, ENEMY_SIZE * 1.2, APPLE_ENEMY_SPEED, pathQueue, APPLE_ENEMY_HEALTH) {
  var imageChanged3 = false
  override val moneyReward: Int = APPLE_ENEMY_MONEY

  override def getHit(damage: Double, slowDown: Double = 0) = {
        getHitFinal(damage, slowDown, APPLE_ENEMY_2_LOC, APPLE_ENEMY_3_LOC)
        if (health >= 0 && health <= (origHealth / 5) && !imageChanged3) {
            imageChanged3 = true
            fill = new ImagePattern(new Image(APPLE_ENEMY_4_LOC))
        }
    }

    def split (): Seq[SplittingEnemy] =
        val enemy1 = new SplittingEnemy(queue)
        val enemy2 = new SplittingEnemy(queue)
        val enemy3 = new SplittingEnemy(queue)
        enemy1.translateX = getGlobalCenter.x + ENEMY_SIZE / 2
        enemy1.translateY = getGlobalCenter.y + ENEMY_SIZE / 2
        enemy2.translateX = getGlobalCenter.x 
        enemy2.translateY = getGlobalCenter.y
        enemy3.translateX = getGlobalCenter.x - ENEMY_SIZE * 1.5
        enemy3.translateY = getGlobalCenter.y - ENEMY_SIZE * 1.5
        Seq(enemy1, enemy2, enemy3)
}
