package Logic

import scalafx.scene.image.ImageView
import _root_.Logic.Logic.GameObject
import scalafx.geometry.Point2D

// Target: add adjustable target????
class Bullet (fileLoc: String, val target: Point2D, speed: Int, val slowDown: Double, val damage: Double, val targetEnemy: Enemy, val boundBox: Double = 0) extends GameObject(fileLoc, 35):
    def move() = 
        val distToTarget = getGlobalCenter.distance(target)
        if (distToTarget > speed) {
            val angle = math.atan2(target.y - getGlobalCenter.y, target.x - getGlobalCenter.x)
            translateX.value += math.cos(angle) * speed
            translateY.value += math.sin(angle) * speed
        } else {
            translateX = 0
            translateY = 0
        }
    end move
    def isOnTarget(): Boolean = 
        val distToTarget = getGlobalCenter.distance(target)
        distToTarget <= speed
    end isOnTarget
end Bullet
