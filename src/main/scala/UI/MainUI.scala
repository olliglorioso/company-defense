package UI

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.Screen


class MainUI extends JFXApp3:

  override def start(): Unit =
    val visualBounds: Rectangle2D = Screen.primary.visualBounds
    val (h, w) = (visualBounds.getHeight, visualBounds.getWidth)

    stage = new JFXApp3.PrimaryStage:
      title = "Company defense"
      scene = mainmenuScene

    lazy val gameplayScene: Scene = new Scene(w, h):
      root = new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("This is gameplay scene")
        )
      }

    lazy val mainmenuScene: Scene = new Scene(w, h):
      root = new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("Main menu"),
          new Button("Start new game") {
            onAction = () => {
              stage.setScene(gameplayScene)
            }
          },
          new Button("Settings") {
            onAction = () => stage.setScene(settingsScene)
          }
        )
      }

    lazy val settingsScene: Scene = new Scene(w, h):
      root = new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("This is settingsscene")
        )
      }

  end start

@main def start() =
  val ui = new MainUI()
  ui.main(Array())
end start


