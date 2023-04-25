package Util

import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}

object Constants {
  // Image locations of enemies
  val BASIC_ENEMY_LOC = "file:src/main/scala/Resources/Enemies/BasicEnemy.jpg"
  val BASIC_ENEMY_2_LOC = "file:src/main/scala/Resources/Enemies/BasicEnemy2.jpg"
  val BASIC_ENEMY_3_LOC = "file:src/main/scala/Resources/Enemies/BasicEnemy3.jpg"
  val SPLITTING_ENEMY_LOC =
    "file:src/main/scala/Resources/Enemies/SplittingEnemy.png"
  val SPLITTING_ENEMY_2_LOC =
    "file:src/main/scala/Resources/Enemies/SplittingEnemy2.png"
  val SPLITTING_ENEMY_3_LOC =
    "file:src/main/scala/Resources/Enemies/SplittingEnemy3.png"
  val SPLITTING_ENEMY_4_LOC =
    "file:src/main/scala/Resources/Enemies/SplittingEnemy4.png"
  val CAMOUFLAGED_ENEMY_LOC =
    "file:src/main/scala/Resources/Enemies/CamouflagedEnemy.png"
  val CAMOUFLAGED_ENEMY_2_LOC =
    "file:src/main/scala/Resources/Enemies/CamouflagedEnemy2.png"
  val CAMOUFLAGED_ENEMY_3_LOC =
    "file:src/main/scala/Resources/Enemies/CamouflagedEnemy3.png"
  val APPLE_ENEMY_LOC = "file:src/main/scala/Resources/Enemies/AppleEnemy.png"
  val APPLE_ENEMY_2_LOC = "file:src/main/scala/Resources/Enemies/AppleEnemy2.png"
  val APPLE_ENEMY_3_LOC = "file:src/main/scala/Resources/Enemies/AppleEnemy3.png"
  val APPLE_ENEMY_4_LOC = "file:src/main/scala/Resources/Enemies/AppleEnemy4.png"
  val TANK_ENEMY_LOC = "file:src/main/scala/Resources/Enemies/TankEnemy.png"
  // Tower images locations
  val REGULAR_TOWER_LOC =
    "file:src/main/scala/Resources/Towers/RegularTower.png"
  val SLOW_DOWN_TOWER_LOC =
    "file:src/main/scala/Resources/Towers/SlowDownTower.png"
  val BOMB_TOWER_LOC =
    "file:src/main/scala/Resources/Towers/BombTower.png"
  // Image locations of other stuff
  val HEALTH_ICON_LOC = "file:src/main/scala/Resources/Other/HealthIcon.png"
  val MONEY_ICON_LOC = "file:src/main/scala/Resources/Other/MoneyIcon.png"
  val SCORE_ICON_LOC = "file:src/main/scala/Resources/Other/ScoreIcon.png"
  val REGULAR_BULLET_LOC = "file:src/main/scala/Resources/Bullets/RegularBullet.png"
  val SLOW_DOWN_BULLET_LOC = "file:src/main/scala/Resources/Bullets/SlowDownBullet.png"
  val BOMB_BULLET_LOC = "file:src/main/scala/Resources/Bullets/BombBullet.png"
  val MAINMENU_BG_LOC = "file:src/main/scala/Resources/Other/MainMenuBackground.png"
  // Scene looks (map, sidebar)
  val MAP_HEIGHT = 12
  val MAP_WIDTH = 20
  val SIDEBAR_WIDTH = 130
  // Player's features in different modes
  val features = Map( // Health, Money, Wave file location, Map location
    1 -> (10, 100, "WaveData/EASY_data.txt", "Maps/EASY_map.txt"), 
    2 -> (5, 50, "WaveData/MEDIUM_data.txt", "Maps/MEDIUM_map.txt"), 
    3 -> (1, 15, "WaveData/HARD_data.txt", "Maps/HARD_map.txt"),
    4 -> (10, 100, "WaveData/CUSTOM_data.txt", "Maps/CUSTOM_map.txt")
  )
  // Costs of towers, R = Regular, S = Slow Down
  val R_COST = 10
  val S_COST = 15
  val B_COST = 20
  // Tower names 
  val R_NAME = "Nerdinator" 
  val S_NAME = "Hacktivist" 
  val B_NAME = "Financebro"
  // Tower ranges
  val R_RANGE = 200 
  val S_RANGE = 200 
  val B_RANGE = 400
  // Tower damages
  val R_DAMAGE = 2
  val S_DAMAGE = 3
  val B_DAMAGE = 10
  // UI looks stuff
  val TOWER_SIDE = 80
  val UI_TILE_SIZE = 89.5
  val ENEMY_SIZE = 89.5
  // Enemy features
  // Health
  val CAMOUFLAGED_ENEMY_HEALTH = 175
  val BASIC_ENEMY_HEALTH = 100
  val SPLITTING_ENEMY_HEALTH = 700
  val APPLE_ENEMY_HEALTH = 1000
  // Seeds
  val CAMOUFLAGED_ENEMY_SPEED = 3
  val BASIC_ENEMY_SPEED = 3
  val SPLITTING_ENEMY_SPEED = 2.2
  val APPLE_ENEMY_SPEED = 1.9
  // Money rewards
  val BASIC_ENEMY_MONEY = 2
  val CAMOUFLAGED_ENEMY_MONEY = 8
  val SPLITTING_ENEMY_MONEY = 6
  val APPLE_ENEMY_MONEY = 10
  // Some locations
  val LATEST_SAVED_LOC = "Saved/LATEST_saved.json"
}
