package Logic

import Util.Constants._

class SlowDownTower() extends Tower(SLOW_DOWN_TOWER_LOC, S_COST, S_RANGE) {
    var lastInit = 0L
    def initBullet(time: Long): Bullet = {
        if (enemyPriority.nonEmpty && canShootTowardsEnemy(enemyPriority.head) && (lastInit == 0 || time - lastInit >= 500000L)) then 
            val closestEnemy = enemyPriority.head
            val towerLoc = localToScene(x.value, y.value)
            val enemyLoc = closestEnemy.localToScene(closestEnemy.x.value, closestEnemy.y.value)

            val bullet = new Bullet(SLOW_DOWN_BULLET_LOC, (enemyLoc.x, enemyLoc.y), 10, 5, 2)
            bullet.x.value = towerLoc.x
            bullet.y.value = towerLoc.y
            lastInit = time
            bullet
        else null
    }
}