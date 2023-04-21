package UI

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import UI._
import UI.GameplayUI
import scalafx.animation.AnimationTimer
import java.util.concurrent.TimeUnit
import Util.State._
import scalafx.stage.Screen
class MainUI extends JFXApp3:

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Company defense"
      resizable = false

    def setSceneTo(scene: Scene): Unit =
      stage.setScene(scene)

    screenHeight.setValue(Screen.primary.visualBounds.getHeight())
    screenWidth.setValue(Screen.primary.visualBounds.getWidth())

    lazy val mainmenuScene: Scene = MainMenuUI(setSceneTo, settingsScene)
    lazy val settingsScene: Scene = SettingsUI(setSceneTo, mainmenuScene)

    stage.scene = mainmenuScene
  end start
end MainUI

@main def start() =
  val ui = new MainUI()
  ui.main(Array())
end start
