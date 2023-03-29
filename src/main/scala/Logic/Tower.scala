package Logic
import Logic.*
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.scene.input.InputIncludes.jfxMouseEvent2sfx

case class Tower (path: String, size: Int, name: String) extends GameObject(path, size, name) {
    onMousePressed = (event) => {
      val x = event.sceneX - layoutX()
      val y = event.sceneY - layoutY()
      translateX = 0
      println(x)
      onMouseDragged = (event) => {
        translateX = event.sceneX - x
        translateY = event.sceneY - y
        println(layoutX)
      }
      onMouseReleased = (event) => {
      }
    }
  
}
