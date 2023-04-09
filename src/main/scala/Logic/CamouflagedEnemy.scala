package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._

class CamouflagedEnemy (pathQueue: Queue[PathTile]) extends Enemy(CAMOUFLAGED_ENEMY_LOC, CAMOUFLAGED_ENEMY_SIZE, CAMOUFLAGED_ENEMY_SPEED, pathQueue, CAMOUFLAGED_ENEMY_HEALTH) {
  override def getHit(damage: Int, slowDown: Int = 0) = {
        getHitFinal(damage, slowDown, CAMOUFLAGED_ENEMY_2_LOC, CAMOUFLAGED_ENEMY_3_LOC)
    }
}
