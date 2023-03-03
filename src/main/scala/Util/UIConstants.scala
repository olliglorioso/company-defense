package Util

import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}


object UIConstants {
    def towerButton(picLoc: String): Button =
        val image = new Image(picLoc)
        val imageView = new ImageView(image) {
            fitWidth = 80
            fitHeight = 80
            preserveRatio = true
        }

        val button = new Button {
            graphic = imageView
            minWidth = 80
            style = "-fx-background-color: grey;"
            minHeight = 80
        }
        button
}
