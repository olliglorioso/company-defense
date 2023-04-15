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

/*
  TestFX GUI testing with JUnit 5.
  TestFX utilizes the dependency injection capabilities provided by JUnit.
*/

//Test class definition. Use JUnit 5 extension mechanic.
@ExtendWith(Array(classOf[ApplicationExtension]))
class MainMenuGui:

  /*
    Our first example test will use a reference to the GUI.
    We cannot create the GUI object directly here, as the
    creation must be done on the JavaFX application thread.

    The start method allows us to do the creation,
    but before start is called, we do not have a reference.

    This could be a use case for Promise, but their value can be assigned only once.
    The start method is called multiple times between tests. We also need the None case
    to fail tests if the GUI is not initialized. This is why Option is used.
  */
  var gui: Option[MainMenuUI] = None

  // Test preparation. Start JavaFX app.
  // Stage is a JavaFX stage injected by TestFX.
  @Start
  def start(stage: Stage): Unit =

    // Create our gui and set it as the Scene of the test Stage
    val newGui = MainMenuUI(stage.asInstanceOf[PrimaryStage], null)
    stage.setScene(newGui)

    // Get a gui reference
    gui = Some(newGui)

    // Remember to show the stage
    stage.show()

  // Option 1: If reference to gui elements is available, testing is quite easy.
  // We can do assertions directly on the GUI Component properties.
  // Robot is injected by TestFX.
  @Test
  def testButton1(robot: FxRobot): Unit =
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

      case None =>

  // Without reference available, we can look up the component in the scene graph.
  @Test
  def testButton2(robot: FxRobot): Unit =

    // With CSS class.
    FxAssert.verifyThat(".button", LabeledMatchers.hasText("Click me!"))
    robot.clickOn(".button")
    FxAssert.verifyThat(".button", LabeledMatchers.hasText("Clicked 1 times"))

    // With CSS id.
    FxAssert.verifyThat(
      "#mainButton",
      LabeledMatchers.hasText("Clicked 1 times")
    )
    robot.clickOn("#mainButton")
    FxAssert.verifyThat(
      "#mainButton",
      LabeledMatchers.hasText("Clicked 2 times")
    )

    // Getting reference without FxAssert. Use javafx button.
    val button: Button = robot.lookup(".button").queryAs(classOf[Button])

    /*
      Find all nodes with css class .button .
      Resulting Java Set is converted to a Scala Set
      by importing scala.jdk.CollectionConverters._
      and using method asScala
    */
    val buttons: scala.collection.mutable.Set[Button] =
      robot.lookup(".button").queryAllAs(classOf[Button]).asScala

    // A predicate can also be used. scala.jdk.OptionConverters._ is imported.
    val noParent: Option[Node] = robot
      .lookup[Node]((n: Node) => n.getParent == null)
      .tryQueryAs(classOf[Node])
      .toScala