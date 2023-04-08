package Logic

import Util.Constants._

class RegularTower() extends Tower(REGULAR_TOWER_LOC, R_COST, R_RANGE) {
    override val damage = R_DAMAGE
    override val slowDown = 0
    override val bulletLoc = REGULAR_BULLET_LOC
}
