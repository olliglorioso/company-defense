package UI

import scalafx.scene.image._
import scalafx.scene.control._
import scala.concurrent.duration.Duration
import javafx.scene.input.MouseEvent
import Logic._
import scalafx.scene.layout._
import scalafx.scene.shape.Circle
import Util.Constants._
import scalafx.beans.property._
import scala.util.control.Breaks._
import scalafx.scene.paint.Color
import Util.HelperFunctions.labelStyle

class TowerButtonUI(
    picLoc: String,
    name: String,
    price: Int,
    desc: String,
    pane: Pane,
    mapInst: GameMap,
    variates: ObjectProperty[Map[String, Double]],
    towersOnMap: BufferProperty[Tower],
    showMessage: (String, String, Int) => Unit,
    openUpgradeMenu: (Tower) => Unit
) extends Button {
  val image = new Image(picLoc)
  val originalPos = (layoutX, layoutY)
  val imageView = new ImageView(image) {
    fitWidth = 80
    fitHeight = 80
    preserveRatio = true
  }
  val tt = new Tooltip()
  tt.setStyle(labelStyle(20))
  tt.setGraphic(new VBox {
    spacing = 5
    children = Seq(
      new Label(name) {
        style = labelStyle(20)
      },
      new Label("Price: " + price),
      new Label(desc)
    )
  })
  tt.setShowDelay(javafx.util.Duration(650.0))
  style = "-fx-background-color: transparent;"
  // scalafx circle with css styling
  val backgroundCircle = new Circle {
    val towerRadius = getBgCircleRadius.toString()
    radius = 1
    fill = Color.Transparent
  }
  graphic = new StackPane {
    children = Seq(backgroundCircle, imageView)
  }
  minWidth = TOWER_SIDE
  minHeight = TOWER_SIDE
  tooltip_=(tt)
  onMousePressed = (event: MouseEvent) => {
    // Record initial position and mouse position
    userData =
      (translateX(), translateY(), event.getSceneX(), event.getSceneY())
  }

  def calculateNewPosition(
      event: MouseEvent,
      userData: Any
  ): (Double, Double) = {
    val (x, y, mouseX, mouseY) =
      userData.asInstanceOf[(Double, Double, Double, Double)]
    val deltaX = mouseX - event.getSceneX()
    val deltaY = mouseY - event.getSceneY()
    (x - deltaX, y - deltaY)
  }

  /**
      * 
      * Does a tower hit a previously placed tower?
      * @param x
      * @param y
      * @return
      */
  def towerCanBePlaced(x: Double, y: Double): Boolean = {
    var broken = false
    breakable {
      towersOnMap.value.forall( tower => {
          // Euclidean distance
          val distance = math.sqrt(math.pow(tower.x.value - x, 2) + math.pow(tower.y.value - y, 2))
          if (distance < (TOWER_SIDE / 2)) then 
            broken = true
            break()
          else
            true
        }
      )
    }
    !broken
  }

  def getBgCircleRadius = {
    name match {
      case R_NAME => (R_RANGE - 1/2 * ENEMY_SIZE).toInt
      case S_NAME => (S_RANGE - 1/2 * ENEMY_SIZE).toInt
    }
  }

  def setBackgroundStyle(event: MouseEvent, mapInst: GameMap): Unit = {
    if (mapInst.isBgTile(event.getSceneY(), event.getSceneX()) && towerCanBePlaced(event.getSceneX() - (TOWER_SIDE / 2), event.getSceneY() - (TOWER_SIDE / 2))) {
      backgroundCircle.fill = Color.Green
    } else {
      backgroundCircle.fill = Color.Red
    }
  }

  def placeNewTowerIfMoney(towerX: Double, towerY: Double, x: Double, y: Double) = {
    if (variates.value("money") >= price) then
      val newTower = name match {
        case R_NAME => new RegularTower(openUpgradeMenu, showMessage)
        case S_NAME => new SlowDownTower(openUpgradeMenu, showMessage)
      }
      towersOnMap.value = towersOnMap.value :+ newTower
      newTower.x = towerX
      newTower.y = towerY
      // Return the button to its original position
      variates.value =
        variates.value.updated("money", variates.value("money") - price)
      pane.children.add(newTower)
      translateX = x
      translateY = y
      style = "-fx-background-color: transparent;"
      openUpgradeMenu(newTower)
  }
    
  onMouseDragged = (event: MouseEvent) => {
    if (variates.value("money") < price) {
      showMessage(
        "Not enough money!",
        "error",
        1
      )
    } else {
      val newPos = calculateNewPosition(event, userData)
      translateX = newPos._1
      translateY = newPos._2
      setBackgroundStyle(event, mapInst)
      val newRadius = getBgCircleRadius
      backgroundCircle.style = "-fx-scale-x: " + newRadius + "; -fx-scale-y: " + newRadius + ";"
    }
  }

  onMouseReleased = (event: MouseEvent) => {
    
    val (x, y, mouseX, mouseY) =
      userData.asInstanceOf[(Double, Double, Double, Double)]
    val towerX = event.getSceneX() - (minWidth() / 2)
    val towerY = event.getSceneY() - (minHeight() / 2)

    def setStartPos() = {
      val transbg = "-fx-background-color: transparent;"
      translateX = x
      translateY = y
      style = transbg
    }
    // Illegal positions not allowed
    if (!mapInst.isBgTile(event.getSceneY(), event.getSceneX())) {
      showMessage(
        "You can't place a tower there!",
        "error",
        1
      )
      setStartPos()
    } else if (!towerCanBePlaced(towerX, towerY)) {
        showMessage(
          "You can't place a tower there!",
          "error",
          1
        )
        setStartPos()
    } else {
      placeNewTowerIfMoney(towerX, towerY, x, y)
    }
    backgroundCircle.fill = Color.Transparent
    backgroundCircle.style = "-fx-scale-x: 0; -fx-scale-y: 0;"
  }
}
