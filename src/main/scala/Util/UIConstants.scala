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
    val BasicEnemy = "file:src/resources/Enemies/BasicEnemy.jpg"
    val SplittingEnemy = "file:src/resources/Enemies/SplittingEnemy.png"
    val CamouflagedEnemy = "file:src/resources/Enemies/CamouflagedEnemy.png"
    val SwarmEnemy = "file:src/resources/Enemies/SwarmEnemy.png"
    val TankEnemy = "file:src/resources/Enemies/TankEnemy.png"
}