package Logic

import scalafx.scene.image.ImageView
import _root_.Logic.Logic.GameObject

// Target: add adjustable target????
class Bullet (fileLoc: String, val target: (Double, Double), speed: Int, slowDown: Int, val damage: Int) extends GameObject(fileLoc, 35) {
    def getDistanceToPoint(x: Double, y: Double): Double = {
        val bulletLoc = localToScene(this.x.value, this.y.value)
        math.sqrt(math.pow(x - bulletLoc.x, 2) + math.pow(y - bulletLoc.y, 2))
    }
    def move(time: Long) = {
        val distToTarget = getDistanceToPoint(target._1, target._2)
        if (distToTarget > speed) {
            val angle = math.atan2(target._2 - y.value, target._1 - x.value)
            translateX.value += math.cos(angle) * speed
            translateY.value += math.sin(angle) * speed
        } else {
            translateX = target._1
            translateY = target._2
        }
    }
    def isOnTarget() = {
        val distToTarget = getDistanceToPoint(target._1, target._2)
        distToTarget <= speed
    }
}
