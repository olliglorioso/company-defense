package Logic

package Logic

import scalafx.scene.image.{ImageView, Image}
import Util.HelperFunctions.getTowerDisplayName
import scalafx.scene.shape.Shape
import javafx.scene.paint.ImagePattern
import scalafx.Includes._
import scalafx.scene.shape.Rectangle

trait GameObject(path: String, size: Double) extends Rectangle:
  fill = new ImagePattern(new Image(path))
  width = size
  height = size
  val displayName = getTowerDisplayName(path)
end GameObject
