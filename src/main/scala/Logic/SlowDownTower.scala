package Logic

import Util.Constants._

class SlowDownTower() extends Tower(SLOW_DOWN_TOWER_LOC, S_COST, S_RANGE) {
    damage = S_DAMAGE
    slowDown = 10
    override val bulletLoc = SLOW_DOWN_BULLET_LOC
}
