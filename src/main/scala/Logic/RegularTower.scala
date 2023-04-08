package Logic

import Util.Constants._

class RegularTower() extends Tower(REGULAR_TOWER_LOC, R_COST, R_RANGE) {
    override val attackSpeed = 5
    override val damage = 1
    override val slowDown = 0
    override val bulletSpeed = 10
    override val bulletLoc = REGULAR_BULLET_LOC
}
