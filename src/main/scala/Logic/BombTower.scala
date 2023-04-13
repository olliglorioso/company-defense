package Logic

import Util.Constants._
import scalafx.Includes._
import javafx.scene.paint.ImagePattern
import scalafx.scene.image.Image

class BombTower(showUpgradeInfo: Tower => Unit, showMessage: (String, String, Int) => Unit) extends Tower(BOMB_TOWER_LOC, S_COST, S_RANGE, showUpgradeInfo, showMessage):
    damage = B_DAMAGE
    slowDown = 0
    attackSpeed = 50
    fill = new ImagePattern(new Image(BOMB_TOWER_LOC) {
        width_=(100)
        height_=(100)
    })
    override val boundBox = 300
    override val bulletLoc = BOMB_BULLET_LOC
end BombTower
