package Logic

package Logic

import scalafx.scene.image.{ImageView, Image}
import Util.HelperFunctions.getTowerDisplayName
import scalafx.scene.shape.Shape
import javafx.scene.paint.ImagePattern
import scalafx.Includes._
import scalafx.scene.shape.Rectangle
import scalafx.geometry.Point2D

trait GameObject(path: String, size: Double) extends Rectangle:
  fill = new ImagePattern(new Image(path))
  width = size
  height = size
  val displayName = getTowerDisplayName(path)
  def getGlobalCenter: Point2D = {
    val loc = localToScene(layoutBounds.getValue().getCenterX(), layoutBounds.getValue().getCenterY())
    loc
  }
end GameObject
