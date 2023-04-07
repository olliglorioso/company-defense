package Logic

package Logic

import scalafx.scene.image.{ImageView, Image}

trait GameObject(path: String, size: Int, name: String) extends ImageView {
  image = new Image(path)
  fitWidth = size
  fitHeight = size
  val displayName = name
}
