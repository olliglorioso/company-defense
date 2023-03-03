package Logic

import scala.collection.mutable.Queue
import java.util.LinkedList

sealed abstract class Mode(val value: Int)
case object Easy extends Mode(1)
case object Medium extends Mode(2)
case object Hard extends Mode(3)

class Gameplay(coins: Int, mapPath: String, mode: Mode) {
  val points = 0
  val map: GameMap = new GameMap(mapPath)
  val towers: Array[Tower] = Array()
  val enemies: Array[Enemy] = Array()
  val bullets: LinkedList[Bullet] = LinkedList()
  val enemyQueue: Queue[Queue[Enemy]] = Queue()

  private def spawnEnemy() = {
    // 
  }

}
