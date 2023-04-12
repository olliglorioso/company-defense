package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen
import Logic._
import UI._
import Util.State._
import Util._
import Util.Constants._ 
import scalafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import Util.Constants.MAINMENU_BG_LOC
import scalafx.scene.image.Image
import scalafx.scene.paint.Paint
import Util.HelperFunctions._
import scala.collection.mutable.ArrayBuffer
import ujson.Obj
import scalafx.scene.control.Alert

class MainMenuUI:
  /** @param stage
    *   PrimaryStage
    * @param gameplayScene
    *   Gameplay-scene
    * @param settingsScene
    *   Settings-scene
    * @return
    *   Mainmenu-scene
    */
  def mainMenuScene(
      stage: JFXApp3.PrimaryStage,
      settingsScene: => Scene
  ): Scene =
    
    val buttonStyle = "-fx-background-color: red; -fx-text-fill: black; -fx-font-size: 24pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
    lazy val settingsSceneLazy = settingsScene
    
    val mainmenuScene: Scene = new Scene(screenWidth.value, screenHeight.value):
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
          new Label("Company Defense") {
            style = "-fx-font-size: 72pt; -fx-text-fill: red; -fx-font-weight: bold; -fx-font-family: 'Impact', sans-serif; -fx-background-color: black; -fx-padding: 0 10px 0 10px; -fx-border-color: red; -fx-border-width: 3px; -fx-border-radius: 10px;"
          },
          new Button("Start new game") {
            style = buttonStyle

            onMouseEntered = () => {
              style = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 30pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
            }

            onMouseExited = () => {
              style = buttonStyle
            }

            onAction = () => {
              try
                variates.setValue(Map("money" -> getMoney, "health" -> getHealth, "waveNo" -> 0, "score" -> 0))
                val gameplayScene = new GameplayUI(stage, mainMenuScene(stage, settingsSceneLazy))
                stage.setScene(gameplayScene)
                gameplayScene.timer.start()
              catch
                case e: Exception => showErrorAlert(e.getMessage())
            }
          },
          new Button("Continue saved game") {
            style = buttonStyle
            onAction = () => {
              try
                val saved = FileHandler().readLinesFromJsonFile(LATEST_SAVED_LOC)
                val (savedMoney, savedHealth, savedDifficulty, savedWaveNo, savedScore, savedTowers) = 
                  (
                    anyToInteger(saved("money")), anyToInteger(saved("health")),
                    anyToInteger(saved("difficulty")), anyToInteger(saved("waveNo")),
                    anyToInteger(saved("score")), saved("towers").arr.asInstanceOf[ArrayBuffer[Obj]]
                  )
                variates.setValue(Map("money" -> savedMoney, "health" -> savedHealth, "waveNo" -> savedWaveNo, "score" -> savedScore))
                difficulty.setValue(savedDifficulty)
                val gameplayScene = new GameplayUI(stage, mainMenuScene(stage, settingsSceneLazy))
                gameplayScene.initializeSavedGame(savedTowers)
                stage.setScene(gameplayScene)
                gameplayScene.timer.start()
              catch
                case e: Exception => showErrorAlert(e.getMessage())
            }
            onMouseEntered = () => {
              style = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 30pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
            }

            onMouseExited = () => {
              style = buttonStyle
            }
          },
          new Button("Settings") {
            style = buttonStyle
            onAction = () => stage.setScene(settingsSceneLazy)
            onMouseEntered = () => {
              style = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 30pt; -fx-font-family: 'Arial Black', sans-serif; -fx-padding: 10px 20px; -fx-background-radius: 50px; -fx-border-radius: 50px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
            }

            onMouseExited = () => {
              style = buttonStyle
            }
          },
        )
      }
    mainmenuScene
  end mainMenuScene
end MainMenuUI
