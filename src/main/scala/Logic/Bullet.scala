package Logic

import scalafx.scene.image.ImageView
import _root_.Logic.Logic.GameObject
import scalafx.geometry.Point2D

// Target: add adjustable target????
class Bullet (fileLoc: String, val target: Point2D, speed: Int, val slowDown: Int, val damage: Int, val targetEnemy: Enemy) extends GameObject(fileLoc, 35) {
    def getDistanceToPoint(x: Double, y: Double): Double = {
        val bulletLoc = localToScene(this.x.value, this.y.value)
        math.sqrt(math.pow(x - bulletLoc.x, 2) + math.pow(y - bulletLoc.y, 2))
    }
    def move(time: Long) = {
        val distToTarget = getDistanceToPoint(target.x, target.y)
        if (distToTarget > speed) {
            val angle = math.atan2(target.y - y.value, target.x - x.value)
            translateX.value += math.cos(angle) * speed
            translateY.value += math.sin(angle) * speed
        } else {
            translateX = target.x
            translateY = target.y
        }
    }
    def isOnTarget() = {
        val distToTarget = getDistanceToPoint(target.x, target.y)
        distToTarget <= speed
    }
}
