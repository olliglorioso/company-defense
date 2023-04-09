package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image.Image

class BasicEnemy (pathQueue: Queue[PathTile]) extends Enemy(BASIC_ENEMY_LOC, BASIC_ENEMY_SIZE, BASIC_ENEMY_SPEED, pathQueue, BASIC_ENEMY_HEALTH) {
    override def getHit(damage: Int, slowDown: Int): Unit = {
        getHitFinal(damage, slowDown, BASIC_ENEMY_2_LOC, BASIC_ENEMY_3_LOC)
    }
}
