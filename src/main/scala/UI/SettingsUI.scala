package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen


class SettingsUI {
    /**
      * 
      *
      * @param stage PrimaryStage
      * @param mainmenuScene Mainmenu-scene
      * @param w Width of the scene
      * @param h Height of the scene
      * @return New settings-scene
      */
    def settingsScene(stage: JFXApp3.PrimaryStage, mainmenuScene: => Scene /* Lazy val as a param */, w: Double, h: Double): Scene =
        lazy val mainmenuSceneLazy = mainmenuScene

        val settingsScene: Scene = new Scene(w, h):
            root = new VBox {
                spacing = 20
                alignment = Pos.Center
                children = Seq(
                    new Label("Main menu"),
                    new Button("Main menu") {
                        onAction = () => stage.setScene(mainmenuSceneLazy)
                    }
                )
        }
        settingsScene
}