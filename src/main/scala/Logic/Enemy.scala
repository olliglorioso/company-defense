package Logic

import scalafx.scene.image.{ImageView, Image}
import Logic.*
import scala.collection.immutable.Queue

case class Enemy(
    path: String,
    size: Int,
    name: String,
    speed1: Int,
    pathQueue: Queue[PathTile],
    health1: Int
) extends GameObject(path, size, name) {
  var health = health1
  var speed = speed1
  var boundBox = 19
  var (nextTile, queue) =
    pathQueue.dequeue // Take start point and create queue class variable.
  var previousTile = nextTile
  getNextTile()

  def getDistanceToPoint (towerX: Double, towerY: Double) = {
    val scenevals = localToScene(x.value, y.value)
    math.sqrt(math.pow(scenevals.x - towerX, 2) + math.pow(scenevals.y - towerY, 2))
  }

  def getNextTile(): PathTile = {
    try
      previousTile = nextTile
      val (next, queueHelper) = queue.dequeue
      nextTile = next
      queue = queueHelper
      next
    catch
      case e: NoSuchElementException =>
        throw Error("No more elements in the queue.")

  }

  def getHit(damage: Int, slowDown: Int = 0): String = {
    if (slowDown > 0) speed = speed - slowDown
    health = health - damage
    if (health < 0) "Remove" else "Nothing"
  }

  // add a health bar next to the neemy
}
