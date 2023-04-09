package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image.Image

class BasicEnemy (pathQueue: Queue[PathTile]) extends Enemy(BASIC_ENEMY_LOC, BASIC_ENEMY_SIZE, BASIC_ENEMY_SPEED, pathQueue, BASIC_ENEMY_HEALTH) {
    override def getHit(damage: Int, slowDown: Int = 0): Unit = {
        if (slowDown > 0) speed = speed - slowDown
            health = health - damage
        if (health >= 0 && health <= origHealth / 2 && !imageChanged) {
            imageChanged = true
            image = new Image(BASIC_ENEMY_2_LOC)
        }
    }
}
