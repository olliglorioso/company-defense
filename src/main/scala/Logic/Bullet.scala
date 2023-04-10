package Logic

import scalafx.scene.image.ImageView
import _root_.Logic.Logic.GameObject
import scalafx.geometry.Point2D

// Target: add adjustable target????
class Bullet (fileLoc: String, val target: Point2D, speed: Int, val slowDown: Int, val damage: Int, val targetEnemy: Enemy) extends GameObject(fileLoc, 35) {
    def move(time: Long) = {
        val distToTarget = getGlobalCenter.distance(target)
        if (distToTarget > speed) {
            val angle = math.atan2(target.y - getGlobalCenter.y, target.x - getGlobalCenter.x)
            translateX.value += math.cos(angle) * speed
            translateY.value += math.sin(angle) * speed
        } else {
            translateX = target.x
            translateY = target.y
        }
    }
    def isOnTarget() = {
        val distToTarget = getGlobalCenter.distance(target)
        distToTarget <= speed
    }
    def getGlobalCenter: Point2D = {
        val bulletLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
        bulletLoc
    }
}
