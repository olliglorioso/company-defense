package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._

class SplittingEnemy (pathQueue: Queue[PathTile]) extends Enemy(SPLITTING_ENEMY_LOC, ENEMY_SIZR, SPLITTING_ENEMY_SPEED, pathQueue, SPLITTING_ENEMY_HEALTH) {
  override def getHit(damage: Int, slowDown: Int = 0) = {
        getHitFinal(damage, slowDown, BASIC_ENEMY_2_LOC, BASIC_ENEMY_3_LOC)
    }
}
