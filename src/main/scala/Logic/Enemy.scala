package Logic

import scalafx.scene.image.{ImageView, Image}
import Logic._
import scala.collection.immutable.Queue
import scala.collection.mutable.Buffer
import scalafx.scene.layout.Pane
import scalafx.beans.property.ObjectProperty
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color
import Util.Constants._
import scalafx.geometry.Point2D
import scalafx.Includes._
import javafx.scene.paint.ImagePattern

case class Enemy(
    path: String,
    size: Double,
    var speed: Double,
    pathQueue: Queue[PathTile],
    var health: Double
) extends GameObject(path, size) {
  val origHealth = health
  var boundBox = 100
  var (nextTile, queue) =
    pathQueue.dequeue // Take start point and create queue class variable.
  var previousTile = nextTile
  var tilesTraversed = 0
  var imageChanged = false
  var imageChanged2 = false
  val moneyReward = 2
  getNextTile()

  def getHit(damage: Double, slowDown: Double): Unit = null

  def sellPrice(): Int = 0

  def getDistanceToPoint (point: Point2D) = {
    val enemyLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
    enemyLoc.distance(point)
  }
  def getGlobalCenter: Point2D = {
        val enemyLoc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
        enemyLoc
  }

  def getHitFinal(damage: Double, slowDown: Double, image1: String, image2: String): Unit = {
      val origSpeed = speed
      health = health - damage
      if (slowDown > 0 && (speed >= (0.6 * origSpeed))) speed = speed * (1 - (slowDown.toDouble / 100))
      if (health >= 0 && health <= origHealth / 2 && !imageChanged) {
          imageChanged = true
          fill = new ImagePattern(new Image(image1))
      }
      if (health >= 0 && health <= origHealth / 4 && !imageChanged2) {
          imageChanged2 = true
          fill = new ImagePattern(new Image(image2))
      }
    }

  def getNextTile(): PathTile = {
    try
      previousTile = nextTile
      val (next, queueHelper) = queue.dequeue
      nextTile = next
      queue = queueHelper
      tilesTraversed += 1
      next
    catch
      case e: NoSuchElementException =>
        throw Error("No more elements in the queue.")

  }

  def move(newEnemies: Buffer[Enemy], pane: Pane, variates: ObjectProperty[Map[String, Double]]): Unit = 
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
        variates.setValue(variates.value.updated("health", variates.value("health") - 1))
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

}
