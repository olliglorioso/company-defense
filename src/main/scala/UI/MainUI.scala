package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen
import UI.MainMenuUI


class MainUI extends JFXApp3:

  override def start(): Unit =
    val visualBounds: Rectangle2D = Screen.primary.visualBounds
    val (h, w) = (visualBounds.getHeight, visualBounds.getWidth)

    stage = new JFXApp3.PrimaryStage:
      title = "Company defense"
      

    lazy val gameplayScene: Scene = new Scene(w, h):
      root = new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("This is gameplay scene")
        )
      }

    lazy val mainmenuScene: Scene = MainMenuUI().mainMenuScene(stage, gameplayScene, settingsScene, w, h)

    lazy val settingsScene: Scene = new Scene(w, h):
      root = new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("This is settingsscene"),
          new Button("Main menu") {
            onAction = () => {
              stage.setScene(mainmenuScene)
            }
          }
        )
      }
    
    stage.scene = mainmenuScene

  end start

@main def start() =
  val ui = new MainUI()
  ui.main(Array())
end start


