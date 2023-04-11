package Logic

import Util.Constants._

class RegularTower(openUpgradeMenu: Tower => Unit) extends Tower(REGULAR_TOWER_LOC, R_COST, R_RANGE) {
    damage = R_DAMAGE
    slowDown = 0
    override val bulletLoc = REGULAR_BULLET_LOC
}
