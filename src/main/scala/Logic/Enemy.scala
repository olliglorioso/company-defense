package Logic

import scalafx.scene.image.{ImageView, Image}
import Logic.*
import scala.collection.immutable.Queue
import scala.collection.mutable.Buffer
import Util.Constants.UI_TILE_SIZE
import scalafx.scene.layout.Pane

case class Enemy(
    path: String,
    size: Int,
    name: String,
    speed1: Int,
    pathQueue: Queue[PathTile],
    health1: Int
) extends GameObject(path, size) {
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

  def move(newEnemies: Buffer[Enemy], pane: Pane): Unit = 
    val (currY, currX) = (translateY.value, translateX.value)
    val (tileY, tileX) = (
      previousTile.coord._1 * UI_TILE_SIZE,
      previousTile.coord._2 * UI_TILE_SIZE
    )
    val (nextTileY, nextTileX) = (
      nextTile.coord._1 * UI_TILE_SIZE,
      nextTile.coord._2 * UI_TILE_SIZE
    )
    val (vecY, vecX) = (currY - tileY, currX - tileX)
    val (dy, dx) = (nextTileY - currY, nextTileX - currX)

    val distance = math.sqrt(dx * dx + dy * dy)
    val (vx, vy) =
      if (distance > 0)
        (
          ((dx / distance) * speed).round.toInt,
          ((dy / distance) * speed).round.toInt
        )
      else (0, 0)

    if (distance <= speed) {
      if (nextTile.getTurn() == End) {
        pane.children.remove(this)
        return
      } else {
        getNextTile()
        newEnemies += this
      }
    } else {
      translateX.value += vx.toInt
      translateY.value += vy
      val angle = (math.atan2(dy, dx) * 180 / math.Pi).round.toInt
      val roundedAngle = (Math.round(angle / 10.0) * 10).toInt
      rotate = roundedAngle
      newEnemies += this
    }

  // add a health bar next to the neemy
}
