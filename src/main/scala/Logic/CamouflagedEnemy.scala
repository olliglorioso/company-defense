package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._

class CamouflagedEnemy (pathQueue: Queue[PathTile]) extends Enemy(CAMOUFLAGED_ENEMY_LOC, CAMOUFLAGED_ENEMY_SIZE, CAMOUFLAGED_ENEMY_SPEED, pathQueue, CAMOUFLAGED_ENEMY_HEALTH) {
  override def getHit(damage: Int, slowDown: Int = 0) = {
        if (slowDown > 0) speed = speed - slowDown
            health = health - damage
        if (health >= 0 && health <= origHealth / 2 && !imageChanged) {
            imageChanged = true
            image = new Image(BASIC_ENEMY_2_LOC)
        }
    }
}
