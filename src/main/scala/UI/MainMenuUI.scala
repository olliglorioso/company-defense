package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen


class MainMenuUI {
    def mainMenuScene(stage: JFXApp3.PrimaryStage, gameplayScene: Scene, settingsScene: Scene, w: Double, h: Double): Scene =
        lazy val gameplaySceneLazy = gameplayScene
        lazy val settingsSceneLazy = settingsScene
        val mainmenuScene: Scene = new Scene(w, h):
            root = new VBox {
                spacing = 20
                alignment = Pos.Center
                children = Seq(
                    new Label("Main menu"),
                    new Button("Start new game") {
                        onAction = () => {
                            println(stage)
                            stage.setScene(gameplaySceneLazy)
                        }
                    },
                    new Button("Settings") {
                        onAction = () => stage.setScene(settingsSceneLazy)
                    }
                )
        }
        mainmenuScene
}