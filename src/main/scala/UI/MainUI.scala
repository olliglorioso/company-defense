package UI

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

class MainUI extends JFXApp3:
  override def start(): Unit =
    print(stage)

    stage = new JFXApp3.PrimaryStage:
      title = "Hello Stage98"
      width = 600
      height = 450

    val root = Pane() // Simple pane component
    val scene = Scene(parent = root) // Scene acts as a container for the scene graph
    stage.scene = scene
  end start

@main def start() =
  val ui = new MainUI()
  ui.main(Array())
end start


