package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.scene.image.Image
import scalafx.scene.paint.Paint
import Util.Constants.MAINMENU_BG_LOC
import scalafx.scene.control.MenuButton
import scalafx.scene.control.MenuItem
import Util.State._
import Util.HelperFunctions.labelStyle
import Util.HelperFunctions.getDifficultyName

class SettingsUI(stage: JFXApp3.PrimaryStage, mainmenuScene: => Scene) extends Scene(screenWidth.value, screenHeight.value) {

  /** @param stage
    *   PrimaryStage
    * @param mainmenuScene
    *   Mainmenu-scene
    * @return
    *   New settings-scene
    */
    lazy val mainmenuSceneLazy = mainmenuScene
    val buttonStyle = "-fx-background-color: red; -fx-text-fill: black; -fx-font-size: 24pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"

    root = new VBox {
      val difficultyInfoLabel = new Label("Difficulty: " + getDifficultyName):
        style = s"${labelStyle(30, "black")} -fx-background-color: red;"
      difficulty.onChange((_, _, newValue) => difficultyInfoLabel.text = "Difficulty: " + getDifficultyName)
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
        new Button("Main menu"):
          onAction = () => stage.setScene(mainmenuSceneLazy)
          style = buttonStyle
          onMouseEntered = () => {
            style = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 30pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
          }

          onMouseExited = () => {
            style = buttonStyle
          }
        ,
        new MenuButton("Difficulty"):
          style = buttonStyle
          items = Seq(
            new MenuItem("Reijon Maansiirto Tmi"):
              onAction = _ => {
                difficulty.setValue(1)
              },
            new MenuItem("IBM & Cisco & BlackBerry"):
              onAction = _ => {
                difficulty.setValue(2)
              },
            new MenuItem("MAGAT"):
              onAction = _ => {
                difficulty.setValue(3)
              },
            new MenuItem("Nokia (Custom)"):
              onAction = _ => {
                difficulty.setValue(4)
              },
          ),
          difficultyInfoLabel
      )
    }
}
