import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.testfx.api.FxRobot
import org.testfx.api.FxAssert
import org.testfx.matcher.control.LabeledMatchers
import javafx.scene.control.Button
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._
import javafx.scene.Node
import UI.MainMenuUI
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.JFXApp3

/*
  TestFX GUI testing with JUnit 5.
  TestFX utilizes the dependency injection capabilities provided by JUnit.
*/

//Test class definition. Use JUnit 5 extension mechanic.
@ExtendWith(Array(classOf[ApplicationExtension]))
class MainMenuGui:

  var gui: Option[MainMenuUI] = None

  // Test preparation. Start JavaFX app.
  @Start
  def start(stage: Stage): Unit =
    val primaryStage = new JFXApp3.PrimaryStage:
      title = "Company defense"
      resizable = false

    val newGui = MainMenuUI(primaryStage, null)
    stage.setScene(newGui)
    gui = Some(newGui)
    stage.show()
  end start

  // Option 1: If reference to gui elements is available, testing is quite easy.
  // We can do assertions directly on the GUI Component properties.
  // Robot is injected by TestFX.
  @Test
  def testStartNewGameButton(robot: FxRobot): Unit =
    gui match
      case Some(gui) =>
        assert(
          gui.startNewGameButton.text() == "Start new game",
          "Start new game button text " + gui.mainLabel.text + "was not Start new game"
        )
        assert(
          gui.startNewGameButton.parent.value != null,
          "Button was not in the scene graph, or it is the root component."
        )
        // Make robot click the button.
        robot.clickOn(gui.startNewGameButton)
      case None => println("No gui")

end MainMenuGui