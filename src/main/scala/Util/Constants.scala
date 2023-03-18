package Util

import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}


object Constants {
    def towerButton(picLoc: String): Button =
        val image = new Image(picLoc)
        val imageView = new ImageView(image) {
            fitWidth = 80
            fitHeight = 80
            preserveRatio = true
        }

        val button = new Button {
            onDragEntered
            graphic = imageView
            minWidth = 80
            style = "-fx-background-color: grey;"
            minHeight = 80
        }
        button
    val BASIC_ENEMY_LOC = "file:src/resources/Enemies/BasicEnemy.jpg"
    val SPLITTING_ENEMY_LOC = "file:src/resources/Enemies/SplittingEnemy.png"
    val CAMOUFLAGED_ENEMY_LOC = "file:src/resources/Enemies/CamouflagedEnemy.png"
    val SWARM_ENEMY_LOC = "file:src/resources/Enemies/SwarmEnemy.png"
    val TANK_ENEMY_LOC = "file:src/resources/Enemies/TankEnemy.png"
    val MAP_HEIGHT = 12
    val MAP_WIDTH = 20
    val SIDEBAR_WIDTH = 130
    val EASY_HP = 100
    val NORMAL_HP = 50
    val HARD_HP = 1
}