

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
import scalafx.geometry.Point2D

@ExtendWith(Array(classOf[ApplicationExtension]))
class EnemyTest:

    /*var gui: Option[MainMenuUI] = None

    @Start
    def start(stage: Stage): Unit =
        
        val newGui = MainMenuUI(null, null)
        stage.setScene(newGui)
        gui = Some(newGui)
        stage.show()*/

    @Test
    def testEnemyInit(): Unit =
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        assert(enemy.health == 100)
        assert(enemy.speed == 5.0)
        assert(enemy.pathQueue == validMap.pathQueue)
        assert(enemy.getGlobalCenter.x == ENEMY_SIZE / 2)
        assert(enemy.getGlobalCenter.y == ENEMY_SIZE / 2)
    
    @Test
    def testGetHit(): Unit = 
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        enemy.getHit(10, 1)
        assert(enemy.health == 90)
        assert(enemy.speed == 4.95)

    @Test
    def testGetDistanceToPoint(): Unit =
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        assert(1 == 500)
    
end EnemyTest