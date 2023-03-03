package Logic

import scalafx.scene.image.{ImageView, Image}
import Logic.*

class Enemy (path: String, size: Int, name: String, speed: Int) extends GameObject(path, size, name) {
    var health = 100
    var speed1 = speed
    var boundBox = 20

    def getHit (damage: Int, slowDown: Int = 0): String = {
        if (slowDown > 0) speed1 = speed1 - slowDown
        health = health - damage
        if (health < 0) "Remove" else "Nothing"
    }
}
