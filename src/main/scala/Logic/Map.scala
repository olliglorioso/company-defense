package Logic

import java.util.logging.FileHandler
import scala.collection.immutable.Queue
import scala.util.control.Breaks._

case class Tile(canBuildTower: Boolean, var coord: Tuple2[Int, Int]) 

sealed abstract class Turn(val value: Int)
case object NoTurn extends Turn(1)
case object TurnRight extends Turn(2)
case object TurnLeft extends Turn(3)
case object Start extends Turn(4)
case object End extends Turn(5)

class PathTile(coord: Tuple2[Int, Int], turn: Turn)
    extends Tile(canBuildTower = false, coord = coord) {
  override def toString = s"PathTile, c: ${coord}, t: ${turn}"
  def getTurn() = turn
}

class BgTile(coord: Tuple2[Int, Int])
    extends Tile(canBuildTower = true, coord = coord) {
  override def toString = s"BgTile, c: ${coord}"
}

val MAP_WIDTH = 20
val MAP_HEIGHT = 12

class GameMap(path: String) {
  var startPoint: PathTile = PathTile((0, 0), Start)
  var endPoint: PathTile = PathTile((0, 0), End)
  var map: Array[Array[Tile]] = initializeMap(path)
  val pathQueue: Queue[PathTile] = generatePathQueue(startPoint)
  
  private def initializeMap(path: String): Array[Array[Tile]] =
    val lines = Util.FileHandler().readLinesFromFile("/Maps/" + path)
    val map = Array.ofDim[Tile](MAP_HEIGHT, MAP_WIDTH)

    if (lines.length != MAP_HEIGHT && lines(0).length != MAP_WIDTH) {
      println("error in map")
    }

    var y = 0
    for (line <- lines) {
      var x = 0
      for (char <- line) {
        char match {
          case '0' => map(y)(x) = new BgTile((y, x))
          case '1' => {
            try
              var turn: Turn = NoTurn
              val pathIsFront = line(x + 1)
              if (pathIsFront != 1) {
                if (lines(y + 1)(x) == 1) then turn = TurnRight
                else if (lines(y - 1)(x) == 1) then turn = TurnLeft
              }
              map(y)(x) = new PathTile((y, x), TurnRight)
            catch
              case _: StringIndexOutOfBoundsException =>
                println("Do not place path to the edges of the map.")
          }
          // Start point > always go straight.
          case '2' => {
            val pt = new PathTile((y, x), Start)
            startPoint.coord = (y, x)
            map(y)(x) = pt
          }
          // End point > always go straight.
          case '3' => {
            val pt = new PathTile((y, x), End)
            endPoint.coord = (y, x)
            map(y)(x) = new PathTile((y, x), End)
          }
        }
        x += 1
      }
      y += 1
    }
    map

  def generatePathQueue (tile: PathTile): Queue[PathTile] = {
    // All possible next steps from a tile.
    val searchValues = Array((0, 1), (0, -1), (1, 0), (-1, 0), (1, 1), (-1, 1), (1, -1), (-1, 1), (-1, -1))
    var currTile = tile
    var prevTile = tile
    var queue: Queue[PathTile] = Queue()
    while (currTile.getTurn() != End) {
      val (y, x) = currTile.coord
      breakable {
        // Go through all possible next tiles
        // -> if it's a PathTile, add it to the queue and move to it.
        // -> otherwise, try other tile.
        for (i <- searchValues) {
          val (editY, editX) = (y + i._1, x + i._2)
          if ((editY, editX) == prevTile.coord) print("")
          else if (editX < 0 || editY < 0 || editX > 19 || editY > 11) print("")
          else {
            val currTile2 = map(editY)(editX)
            if (currTile2.isInstanceOf[PathTile]) {
              val ptObject = currTile2.asInstanceOf[PathTile]
              queue = queue.enqueue(ptObject)
              if (currTile.coord == startPoint.coord) {
                prevTile = startPoint
              } else {
                prevTile = currTile
              }
              currTile = ptObject
              break
            }
          }
        }
      }
    }
    return queue
  }
}

@main def koira = GameMap("test_map.txt")