package Logic

import Util.Constants._
import scala.collection.immutable.Queue
import scalafx.scene.image._

class CamouflagedEnemy (pathQueue: Queue[PathTile]) extends Enemy(CAMOUFLAGED_ENEMY_LOC, ENEMY_SIZE, CAMOUFLAGED_ENEMY_SPEED, pathQueue, CAMOUFLAGED_ENEMY_HEALTH):
  override val moneyReward: Int = 4
  override def getHit(damage: Double, slowDown: Double) =
        getHitFinal(damage, slowDown, CAMOUFLAGED_ENEMY_2_LOC, CAMOUFLAGED_ENEMY_3_LOC)
  end getHit
end CamouflagedEnemy