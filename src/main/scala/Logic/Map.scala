package Logic

import java.util.logging.FileHandler

class Tile (canBuildTower: Boolean, coord: Tuple2[Int, Int]) 

object Turn extends Enumeration {
    type Turn = Value
    val NoTurn = Value
    val TurnRight = Value
    val TurnLeft = Value
}

class PathTile (coord: Tuple2[Int, Int], turn: Turn.Turn) extends Tile(canBuildTower = false, coord = coord) {
  override def toString = s"PathTile, c: ${coord}, t: ${turn}"
}

class BgTile (coord: Tuple2[Int, Int]) extends Tile(canBuildTower = true, coord = coord)  {
  override def toString = s"BgTile, c: ${coord}"
}

val MAP_WIDTH = 32
val MAP_HEIGHT = 18

class GameMap(path: String) {
  var map: Array[Array[Tile]] = this.initializeMap(path)
  var startPoint: Tuple2[Int, Int] = (0, 0)
  var endPoint: Tuple2[Int, Int] = (0, 0)
  
  private def initializeMap (path: String): Array[Array[Tile]] = 
    val map = Array.ofDim[Tile](MAP_WIDTH, MAP_HEIGHT)
    val lines = Util.FileHandler().readLinesFromFile("test_map.txt")
    var y = 0
    for (line <- lines) {
      var x = 0
      for (char <- line) {
        char match {
          case '0' => map(x)(y) = new BgTile((x, y))
          case '1' => {
            try
              var turn = Turn.NoTurn
              val pathIsFront = line(x + 1)
              if (pathIsFront != 1) {
                if (lines(y + 1)(x) == 1) then turn = Turn.TurnRight
                else if (lines(y - 1)(x) == 1) then turn = Turn.TurnLeft
              }
              map(x)(y) = new PathTile((x, y), Turn.TurnRight)
            catch
              case _: StringIndexOutOfBoundsException => print("Do not place path to the edges of the map.")
          }
          // Start point > always go straight.
          case '2' => {
            this.startPoint = (x, y)
            map(x)(y) = new PathTile((x, y), Turn.NoTurn)
          }
          // End point > always go straight.
          case '3' => {
            this.endPoint = (x, y)
            map(x)(y) = new PathTile((x, y), Turn.NoTurn)
          }
        }
        x += 1
      }
      y += 1
    }
    print(map(0)(1))
    map
}


@main def koira = GameMap("test_map.txt")