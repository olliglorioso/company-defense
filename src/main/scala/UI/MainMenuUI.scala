package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen
import Logic.*
import UI.*
import scalafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import Util.Constants.MAINMENU_BG_LOC
import scalafx.scene.image.Image
import scalafx.scene.paint.Paint

class MainMenuUI {

  /** @param stage
    *   PrimaryStage
    * @param gameplayScene
    *   Gameplay-scene
    * @param settingsScene
    *   Settings-scene
    * @param w
    *   Width of the scene
    * @param h
    *   Height of the scene
    * @return
    *   Mainmenu-scene
    */
  def mainMenuScene(
      stage: JFXApp3.PrimaryStage,
      gameplayScene: GameplayUI,
      settingsScene: Scene,
      w: Double,
      h: Double
  ): Scene =
    lazy val gameplaySceneLazy = gameplayScene
    lazy val settingsSceneLazy = settingsScene
    val mainmenuScene: Scene = new Scene(w, h):
      root = new VBox {
        background = new Background(
          fills = Seq(
            new BackgroundFill(
              fill = Paint.valueOf("#000000"),
              radii = new CornerRadii(0),
              insets = Insets.Empty
            )
          ),
          images = Seq(
            new BackgroundImage(
              image = new Image(MAINMENU_BG_LOC),
              repeatX = BackgroundRepeat.NoRepeat,
              repeatY = BackgroundRepeat.NoRepeat,
              position = BackgroundPosition.Center,
              size = BackgroundSize.Default
            )
          )
        )
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("Main menu"),
          new Button("Start new game") {
            onAction = () => {
              stage.setScene(gameplaySceneLazy)
              gameplaySceneLazy.timer.start()
            }
          },
          new Button("Settings") {
            onAction = () => stage.setScene(settingsSceneLazy)
          }
        )
      }
    mainmenuScene
}
