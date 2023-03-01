package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen


class GameplayUI {
    /**
      * 
      *
      * @param stage PrimaryStage
      * @param w Width of the scene
      * @param h Height of the scene
      * @return Gameplay-scene
      */
    def gameplayScene(stage: JFXApp3.PrimaryStage, w: Double, h: Double): Scene =
        val mainmenuScene: Scene = new Scene(w, h):
            root = new VBox {
                spacing = 20
                alignment = Pos.Center
                children = Seq(
                    new Label("Gameplay"),
                )
        }
        mainmenuScene
}