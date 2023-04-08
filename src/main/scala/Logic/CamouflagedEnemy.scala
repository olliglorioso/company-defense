package Logic

import Util.Constants._
import scala.collection.immutable.Queue

class CamouflagedEnemy (pathQueue: Queue[PathTile]) extends Enemy(CAMOUFLAGED_ENEMY_LOC, CAMOUFLAGED_ENEMY_SIZE, CAMOUFLAGED_ENEMY_SPEED, pathQueue, CAMOUFLAGED_ENEMY_HEALTH) {
  
}
