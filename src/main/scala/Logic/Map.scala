package Logic

import java.util.logging.FileHandler
import scala.collection.immutable.Queue
import scala.util.control.Breaks._
import Util.Constants._
import Util.HelperFunctions.showErrorAlert

case class Tile(canBuildTower: Boolean, var coord: Tuple2[Int, Int])
end Tile

sealed abstract class Turn(val value: Int)
case object NoTurn extends Turn(1)
case object TurnRight extends Turn(2)
case object TurnLeft extends Turn(3)
case object Start extends Turn(4)
case object End extends Turn(5)


/**
  * Path tile (enemy's route tile).
  *
  * @param coord Coodinates
  * @param turn Where is the next PathTile.
  */
class PathTile(coord: Tuple2[Int, Int], val turn: Turn)
    extends Tile(canBuildTower = false, coord = coord):
  override def toString = s"PathTile, c: ${coord}, t: ${turn}"
  def getTurn() = turn
end PathTile

/**
  * Background tile.
  *
  * @param coord Coordinates
  */
class BgTile(coord: Tuple2[Int, Int])
    extends Tile(canBuildTower = true, coord = coord):
  override def toString = s"BgTile, c: ${coord}"
end BgTile

class GameMap(path: String):
  var startPoint: PathTile = PathTile((0, 0), Start)
  var endPoint: PathTile = PathTile((0, 0), End)
  var map: Array[Array[Tile]] = null
  try 
    map = initializeMap(path)
  catch
    case e: Exception => {
      showErrorAlert(e.getMessage)
    }
  val pathQueue: Queue[PathTile] = generatePathQueue(startPoint)

  /** @param path
    *   Path to the map file location.
    * @return
    *   2D Array of BgTiles and PathTiles.
    */
  def initializeMap(path: String): Array[Array[Tile]] =
    val lines = Util.FileHandler().readLinesFromFile(path)
    if (
      lines.length != MAP_HEIGHT || lines
        .filter(a => a.length == MAP_WIDTH)
        .length != lines.length
    ) then throw Exception("Invalid map file.")
    val map = Array.ofDim[Tile](MAP_HEIGHT, MAP_WIDTH)
    var y = 0
    for (line <- lines) {
      var x = 0
      for (char <- line) {
        char match {
          case '0' => map(y)(x) = new BgTile((y, x))
          case '1' => {
            // If it's a PathTile, then check surrounding tiles and find the next tile, and determine the turn.
            try
              var turn: Turn = NoTurn
              var invalidPath = false
              val pathIsFront = line.charAt(x + 1)
              if (pathIsFront != '1') {
                invalidPath = true
                if (lines(y + 1).charAt(x) == '1') then
                  turn = TurnRight
                  invalidPath = false
                else if (lines(y - 1).charAt(x) == '1') then
                  turn = TurnLeft
                  invalidPath = false
                else if (line.charAt(x - 1) == '1') then invalidPath = false
              }
              if (invalidPath) then throw Exception("Invalid map file (invalid path).")
              else map(y)(x) = new PathTile((y, x), turn)
            catch
              case _: StringIndexOutOfBoundsException => throw Exception("Invalid map file.")
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
          case somethingElse => throw Exception("Invalid map file (invalid character).")
        }
        x += 1
      }
      y += 1
    }
    map
  end initializeMap

  /** @param tile
    *   The starting tile.
    * @return
    *   A queue of PathTiles.
    */
  private def generatePathQueue(tile: PathTile): Queue[PathTile] = 
    // All possible next steps from a tile.
    val searchValues = Array(
      (0, 1),
      (0, -1),
      (1, 0),
      (-1, 0),
      (1, 1),
      (-1, 1),
      (1, -1),
      (-1, 1),
      (-1, -1)
    )
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
          if ((editY, editX) == prevTile.coord) None
          else if (editX < 0 || editY < 0 || editX > 19 || editY > 11) None
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
  end generatePathQueue

  /**
    * Returns the tile that is located in the given coordinates.
    *
    * @return Tile
    */
  private def whichTile (x: Int, y: Int): Tile = map(x)(y)
  end whichTile

  /** @param coords
    *   The coordinates to check, not rounded.
    * @return
    *   True if the tile is BgTile, false otherwise (PathTile / Sidebar area).
    */
  def isBgTile (x: Double, y: Double): Boolean =
    val xCoord = (x / UI_TILE_SIZE).toInt
    val yCoord = (y / UI_TILE_SIZE).toInt
    val invalidValues = xCoord < 0 || yCoord < 0 || xCoord > (MAP_HEIGHT - 1) || yCoord > (MAP_WIDTH - 1)

    if invalidValues then false
    else
      val tile = whichTile(xCoord, yCoord)
      if (tile.isInstanceOf[BgTile]) true
      else false
  end isBgTile
end GameMap
