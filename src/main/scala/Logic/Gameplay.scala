package Logic

import scala.collection.mutable.Queue
import java.util.LinkedList

object Mode extends Enumeration {
    type Mode = Value
    val Easy = Value("Reijon Maansiirto")
    val Medium = Value("IBM & Nokia & BlackBerry")
    val Hard = Value("MAGAT")
}


class Gameplay (coins: Int, mapPath: String, mode: Mode.Mode) {
    val points = 0
    val map: GameMap = new GameMap(mapPath)
    val towers: Array[Tower] = Array()
    val enemies: Array[Enemy] = Array()
    val bullets: LinkedList[Bullet] = LinkedList()
    val enemyQueue: Queue[Queue[Enemy]] = Queue()

}
