package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._

class SplittingEnemy (pathQueue: Queue[PathTile]) extends Enemy(SPLITTING_ENEMY_LOC, ENEMY_SIZE, SPLITTING_ENEMY_SPEED, pathQueue, SPLITTING_ENEMY_HEALTH) {
  var imageChanged3 = false
  val origSpeed = speed
  override val moneyReward: Int = 7

  override def getHit(damage: Int, slowDown: Int = 0) = {
        getHitFinal(damage, slowDown, SPLITTING_ENEMY_2_LOC, SPLITTING_ENEMY_3_LOC)
    }

  override def getHitFinal(damage: Int, slowDown: Int, image1: String, image2: String): Unit = {
        health = health - damage
        if (slowDown > 0 && speed >= (0.5 * origSpeed)) then speed = speed * (1 - (slowDown.toDouble / 100))

        if (health >= 0 && health <= (origHealth / 1.5) && !imageChanged) {
            imageChanged = true
            image = new Image(image1)
        }
        if (health >= 0 && health <= (origHealth / 2) && !imageChanged2) {
            imageChanged2 = true
            image = new Image(image2)
        }
        if (health >= 0 && health <= (origHealth / 4) && !imageChanged3) {
            imageChanged3 = true
            image = new Image(SPLITTING_ENEMY_4_LOC)
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
