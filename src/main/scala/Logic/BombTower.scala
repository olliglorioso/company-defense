package Logic

import Util.Constants._

class BombTower(showUpgradeInfo: Tower => Unit, showMessage: (String, String, Int) => Unit) extends Tower(BOMB_TOWER_LOC, S_COST, S_RANGE, showUpgradeInfo, showMessage):
    damage = B_DAMAGE
    slowDown = 0
    attackSpeed = 15
    override val boundBox = 500
    override val bulletLoc = BOMB_BULLET_LOC
end BombTower
