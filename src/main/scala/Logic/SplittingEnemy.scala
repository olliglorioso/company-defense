package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._
import scalafx.Includes._
import javafx.scene.paint.ImagePattern

class SplittingEnemy (pathQueue: Queue[PathTile]) extends Enemy(SPLITTING_ENEMY_LOC, ENEMY_SIZE, SPLITTING_ENEMY_SPEED, pathQueue, SPLITTING_ENEMY_HEALTH) {
  var imageChanged3 = false
  val origSpeed = speed
  override val moneyReward: Int = 7

  override def getHit(damage: Double, slowDown: Double = 0) = {
        getHitFinal(damage, slowDown, SPLITTING_ENEMY_2_LOC, SPLITTING_ENEMY_3_LOC)
        if (health >= 0 && health <= (origHealth / 4) && !imageChanged3) {
            imageChanged3 = true
            fill = new ImagePattern(new Image(SPLITTING_ENEMY_4_LOC))
        }
    }

    def split (): Seq[BasicEnemy] =
        val enemy1 = new BasicEnemy(pathQueue)
        val enemy2 = new BasicEnemy(pathQueue)
        val enemy3 = new BasicEnemy(pathQueue)
        val sceneLoc = localToScene(x.value, y.value)
        enemy1.x = translateX.value
        enemy1.y = translateY.value
        enemy2.x = translateX.value
        enemy2.y = translateY.value
        enemy3.x = translateX.value
        enemy3.y = translateY.value
        Seq(enemy1, enemy2, enemy3)
}
