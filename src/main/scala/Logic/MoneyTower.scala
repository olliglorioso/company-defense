package Logic

import Util.Constants._
import scalafx.Includes._
import javafx.scene.paint.ImagePattern
import scalafx.scene.image.Image
import Util.State._

class MoneyTower(showUpgradeInfo: Tower => Unit, showMessage: (String, String, Int) => Unit) extends Tower(MONEY_TOWER_LOC, M_COST, 0, showUpgradeInfo, showMessage):
    slowDown = 0
    attackSpeed = 20
    damage = 0
    var moneyIncrease = 3.0
    fill = new ImagePattern(new Image(MONEY_TOWER_LOC) {
        width_=(100)
        height_=(100)
    })

    private def upgradeFeatures(): Unit =
        level.value += 1
        attackSpeed *= 0.97
        moneyIncrease += 1.0
    end upgradeFeatures

    override def initBullet(time: Long): Bullet = 
        if ((lastBulletInit == 0L || time - lastBulletInit >= attackSpeed * 100000000L))
            variates.setValue(variates.value.updated("money", variates.value("money") + moneyIncrease))
            lastBulletInit = time
        null
    end initBullet

    override def addEnemyToPriorityQueue(enemy: Enemy): Unit = null
    override def clearPriorityQueue(): Unit = null
    override def rotateTowardsPriorityEnemy(): Unit = null
end MoneyTower
