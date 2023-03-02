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

val MAP_WIDTH = 20
val MAP_HEIGHT = 12

class GameMap(path: String) {
  var map: Array[Array[Tile]] = this.initializeMap(path)
  var startPoint: Tuple2[Int, Int] = (0, 0)
  var endPoint: Tuple2[Int, Int] = (0, 0)
  
  private def initializeMap (path: String): Array[Array[Tile]] = 
    val lines = Util.FileHandler().readLinesFromFile("test_map.txt")
    val map = Array.ofDim[Tile](MAP_HEIGHT, MAP_WIDTH)
    
    if (lines.length != 12 && lines(0).length != 20) {
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
              var turn = Turn.NoTurn
              val pathIsFront = line(x + 1)
              if (pathIsFront != 1) {
                if (lines(y + 1)(x) == 1) then turn = Turn.TurnRight
                else if (lines(y - 1)(x) == 1) then turn = Turn.TurnLeft
              }
              map(y)(x) = new PathTile((y, x), Turn.TurnRight)
            catch
              case _: StringIndexOutOfBoundsException => println("Do not place path to the edges of the map.")
          }
          // Start point > always go straight.
          case '2' => {
            this.startPoint = (y, x)
            map(y)(x) = new PathTile((y, x), Turn.NoTurn)
          }
          // End point > always go straight.
          case '3' => {
            this.endPoint = (y, x)
            map(y)(x) = new PathTile((y, x), Turn.NoTurn)
          }
        }
        x += 1
      }
      y += 1
    }
    map
}


@main def koira = GameMap("test_map.txt")