package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen
import UI._

import UI.GameplayUI
import scalafx.animation.AnimationTimer
import java.util.concurrent.TimeUnit

class MainUI extends JFXApp3:
  main(Array())

  override def start(): Unit =
    val visualBounds: Rectangle2D = Screen.primary.visualBounds
    val (h, w) = (visualBounds.getHeight, visualBounds.getWidth)

    stage = new JFXApp3.PrimaryStage:
      title = "Company defense"
      resizable = false

    lazy val gameplayScene: GameplayUI = new GameplayUI(w, h)
    lazy val mainmenuScene: Scene =
      MainMenuUI().mainMenuScene(stage, gameplayScene, settingsScene, w, h)
    lazy val settingsScene: Scene =
      SettingsUI().settingsScene(stage, mainmenuScene, w, h)

    stage.scene = mainmenuScene
  end start

@main def start() =
  val ui = new MainUI()
  ui.main(Array())
end start
