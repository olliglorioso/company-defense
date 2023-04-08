package Logic

import Util.Constants._

class SlowDownTower() extends Tower(SLOW_DOWN_TOWER_LOC, S_COST, S_RANGE) {
    override val attackSpeed = 2
}
