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
    
    screenHeight.setValue(Screen.primary.visualBounds.getHeight())
    screenWidth.setValue(Screen.primary.visualBounds.getWidth())

    lazy val mainmenuScene: Scene = MainMenuUI(stage, settingsScene)
    lazy val settingsScene: Scene = SettingsUI(stage, mainmenuScene)

    stage.scene = mainmenuScene
  end start

@main def start() =
  val ui = new MainUI()
  ui.main(Array())
end start
