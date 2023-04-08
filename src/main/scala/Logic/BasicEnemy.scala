package Logic

import Util.Constants._
import scala.collection.immutable.Queue

class BasicEnemy (pathQueue: Queue[PathTile]) extends Enemy(BASIC_ENEMY_LOC, BASIC_ENEMY_SIZE, BASIC_ENEMY_SPEED, pathQueue, BASIC_ENEMY_HEALTH) {
}
