package Logic

import Util.Constants._

class SlowDownTower(showUpgradeInfo: Tower => Unit) extends Tower(SLOW_DOWN_TOWER_LOC, S_COST, S_RANGE, showUpgradeInfo) {
    damage = S_DAMAGE
    slowDown = 2
    override val bulletLoc = SLOW_DOWN_BULLET_LOC
}
