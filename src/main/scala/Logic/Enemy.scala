package Logic

import scalafx.scene.image.{ImageView, Image}
import Logic.*
import scala.collection.immutable.Queue

case class Enemy(
    path: String,
    size: Int,
    name: String,
    speed: Int,
    pathQueue: Queue[PathTile]
) extends GameObject(path, size, name) {
  var health = 100
  var speed1 = speed
  var boundBox = 19
  var (nextTile, queue) =
    pathQueue.dequeue // Take start point and create queue class variable.
  var previousTile = nextTile
  getNextTile()

  def getDistanceToPoint (towerX: Double, towerY: Double) = math.sqrt(math.pow(x.value - towerX, 2) + math.pow(y.value - towerY, 2))

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
    if (slowDown > 0) speed1 = speed1 - slowDown
    health = health - damage
    if (health < 0) "Remove" else "Nothing"
  }
}
