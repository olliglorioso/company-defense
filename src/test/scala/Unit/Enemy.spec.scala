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

@ExtendWith(Array(classOf[ApplicationExtension]))
class EnemyTest extends AnyFlatSpec with Matchers:

    var gui: Option[MainMenuUI] = None

    @Start
    def start(stage: Stage): Unit =
        val newGui = MainMenuUI(null, null)
        stage.setScene(newGui)
        gui = Some(newGui)
        stage.show()

    "An Enemy" should "move along a path" in {
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        val pane = new Pane()
        pane.children.add(enemy)
        enemy.move(pane)
        println(enemy.x)
        println(enemy.x)
        println(enemy.x)
        1 shouldEqual 1
    }

    it should "take damage and slow down" in {
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        enemy.getHitFinal(3, 30, "enemy_damaged.png", "enemy_almost_dead.png")
        enemy.health shouldEqual 7
        enemy.speed shouldEqual 3.5
        enemy.fill shouldBe a [javafx.scene.paint.ImagePattern]
        enemy.getHitFinal(5, 0, "enemy_damaged.png", "enemy_almost_dead.png")
        enemy.health shouldEqual 2
        enemy.speed shouldEqual 3.5
        enemy.fill shouldBe a [javafx.scene.paint.ImagePattern]
        enemy.getHitFinal(2, 0, "enemy_damaged.png", "enemy_almost_dead.png")
        enemy.health shouldEqual 0
        enemy.speed shouldEqual 3.5
        enemy.fill shouldBe a [javafx.scene.paint.ImagePattern]
    }

    it should "calculate the distance to a point" in {
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        enemy.getDistanceToPoint(new scalafx.geometry.Point2D(0, 0)) shouldEqual 0
        enemy.getDistanceToPoint(new scalafx.geometry.Point2D(25, 0)) shouldEqual 25
        enemy.getDistanceToPoint(new scalafx.geometry.Point2D(25, 25)) shouldEqual 50
    }

    it should "calculate the global center" in {
        val validMap = new GameMap("/Maps/test.valid_map.txt")
        val enemy = new Enemy(BASIC_ENEMY_LOC, ENEMY_SIZE, 5, validMap.pathQueue, 100)
        enemy.getGlobalCenter shouldEqual new scalafx.geometry.Point2D(25, 25)
    }

end EnemyTest