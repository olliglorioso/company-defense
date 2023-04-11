package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen
import scalafx.scene.image.Image
import scalafx.scene.paint.Paint
import Util.Constants.MAINMENU_BG_LOC

class SettingsUI {

  /** @param stage
    *   PrimaryStage
    * @param mainmenuScene
    *   Mainmenu-scene
    * @param w
    *   Width of the scene
    * @param h
    *   Height of the scene
    * @return
    *   New settings-scene
    */
  def settingsScene(
      stage: JFXApp3.PrimaryStage,
      mainmenuScene: => Scene /* Lazy val as a param */,
      w: Double,
      h: Double
  ): Scene =
    lazy val mainmenuSceneLazy = mainmenuScene
    val buttonStyle = "-fx-background-color: red; -fx-text-fill: black; -fx-font-size: 24pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"

    val settingsScene: Scene = new Scene(w, h):
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
          new Button("Main menu") {
            onAction = () => stage.setScene(mainmenuSceneLazy)
            style = buttonStyle
          }
          // dropdown menu for selecting difficulty
        )
      }
    settingsScene
}
