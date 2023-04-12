package Logic

import UI.MainUI
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

class Game:
  // Start the game and UI.
  def startProgram(): Unit =
    val ui = new MainUI()
    ui.main(Array())
  end startProgram
end Game

@main def start() =
  val game = new Game()
  game.startProgram()
end start
