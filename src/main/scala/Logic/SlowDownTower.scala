package Logic

import Util.Constants._

class SlowDownTower() extends Tower(SLOW_DOWN_TOWER_LOC, S_COST, S_RANGE) {
    override val attackSpeed = 5
    override val damage = 1
    override val slowDown = 5
    override val bulletSpeed = 10
    override val bulletLoc = SLOW_DOWN_BULLET_LOC
}
