package Unit

package Logic

import scalafx.scene.layout.Pane
import scala.collection.immutable.Queue
import collection.mutable.Buffer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Util.Constants._
import _root_.Logic._
import org.junit.jupiter.api.extension.ExtendWith
import scalafx.application.JFXApp3

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
import UI._
import org.scalatest.BeforeAndAfterEach

@ExtendWith(Array(classOf[ApplicationExtension]))
class EnemyTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:

    var gui: Option[MainMenuUI] = None
    var enemy: Enemy = null
    var validMap: GameMap = null

    @Start
    def start(stage: Stage): Unit =
        val newGui = MainMenuUI(null, null)
        stage.setScene(newGui)
        gui = Some(newGui)
        stage.show()

    override def beforeEach(): Unit =
        validMap = new GameMap("/Maps/test.valid_map.txt")
        enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)

    override def afterEach(): Unit =
        enemy = null
        validMap = null

    @Test
    def testEnemyInit(): Unit =
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        enemy.health should be (100)
        enemy.speed should be (5.0)
        enemy.pathQueue should be (validMap.pathQueue)
        enemy.getGlobalCenter.x should be (ENEMY_SIZE / 2)
        enemy.getGlobalCenter.y should be (ENEMY_SIZE / 2)
    
end EnemyTest