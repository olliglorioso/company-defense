package UI

import scalafx.scene.control.Button
import scalafx.scene.image._

class TowerButtonUI(picLoc: String) extends Button {
    val image = new Image(picLoc)
    val imageView = new ImageView(image) {
        fitWidth = 80
        fitHeight = 80
        preserveRatio = true
    }
    graphic = imageView
    minWidth = 80
    style = "-fx-background-color: grey;"
    minHeight = 80
}
