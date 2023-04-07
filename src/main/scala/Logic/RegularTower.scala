package Logic

import Util.Constants._

class RegularTower() extends Tower(REGULAR_TOWER_LOC, R_COST, R_RANGE) {
    var lastInit = 0L
    def initBullet(time: Long): Bullet = {
            if (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head) && (lastInit == 0 || time - lastInit >= 1000000000L)) then 
                val closestEnemy = enemyPriority.head
                val towerLoc = localToScene(x.value, y.value)
                val enemyLoc = closestEnemy.localToScene(closestEnemy.x.value, closestEnemy.y.value)

                val bullet = new Bullet(REGULAR_BULLET_LOC, (enemyLoc.x, enemyLoc.y), 10, 0, 1)
                bullet.x.value = towerLoc.x
                bullet.y.value = towerLoc.y
                lastInit = time
                bullet
            else null
        }
}
