package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._

class SplittingEnemy (pathQueue: Queue[PathTile]) extends Enemy(SPLITTING_ENEMY_LOC, SPLITTING_ENEMY_SIZE, SPLITTING_ENEMY_SPEED, pathQueue, SPLITTING_ENEMY_HEALTH) {
  override def getHit(damage: Int, slowDown: Int = 0) = {
        if (slowDown > 0) speed = speed - slowDown
            health = health - damage
        if (health >= 0 && health <= origHealth / 2 && !imageChanged) {
            imageChanged = true
            image = new Image(BASIC_ENEMY_2_LOC)
        }
    }
}
