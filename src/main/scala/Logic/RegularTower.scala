package Logic

import Util.Constants._

class RegularTower(showUpgradeInfo: Tower => Unit, showMessage: (String, String, Int) => Unit) extends Tower(REGULAR_TOWER_LOC, R_COST, R_RANGE, showUpgradeInfo, showMessage):
    damage = R_DAMAGE
    slowDown = 0
    override val bulletLoc = REGULAR_BULLET_LOC
end RegularTower
