package Logic

package Logic

import scalafx.scene.image.{ImageView, Image}

trait GameObject(path: String, size: Double) extends ImageView {
  image = new Image(path)
  fitWidth = size
  fitHeight = size
}
