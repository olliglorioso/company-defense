package Util

import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}

object Constants {
  val BASIC_ENEMY_LOC = "file:src/main/scala/Resources/Enemies/BasicEnemy.jpg"
  val SPLITTING_ENEMY_LOC =
    "file:src/main/scala/Resources/Enemies/SplittingEnemy.png"
  val CAMOUFLAGED_ENEMY_LOC =
    "file:src/main/scala/Resources/Enemies/CamouflagedEnemy.png"
  val SWARM_ENEMY_LOC = "file:src/main/scala/Resources/Enemies/SwarmEnemy.png"
  val TANK_ENEMY_LOC = "file:src/main/scala/Resources/Enemies/TankEnemy.png"
  val REGULAR_TOWER_LOC =
    "file:src/main/scala/Resources/Towers/RegularTower.png"
  val SLOW_DOWN_TOWER_LOC =
    "file:src/main/scala/Resources/Towers/SlowDownTower.png"
  val HEALTH_ICON_LOC = "file:src/main/scala/Resources/Other/HealthIcon.png"
  val MONEY_ICON_LOC = "file:src/main/scala/Resources/Other/MoneyIcon.png"
  val SCORE_ICON_LOC = "file:src/main/scala/Resources/Other/ScoreIcon.png"
  val MAP_HEIGHT = 12
  val MAP_WIDTH = 20
  val SIDEBAR_WIDTH = 130
  val EASY_HP = 100
  val NORMAL_HP = 50
  val HARD_HP = 1
  val R_COST = 10 // Regular Tower cost
  val S_COST = 15 // Slow Down Tower cost
  val R_NAME = "Nerdinator" // Regular Tower name
  val S_NAME = "Hacktivist" // Slow Down Tower name
  val TOWER_SIDE = 80
  val UI_TILE_SIZE = 89.5
}
