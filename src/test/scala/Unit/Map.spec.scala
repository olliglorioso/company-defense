import collection.mutable.Buffer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Util.Constants._
import Logic._

class MapTest extends AnyFlatSpec with Matchers:
  "Map" should "check if the text file for map is correctly formatted." in {
    assertThrows[InvalidMapError] {
      val map = new GameMap("test.invalid_map.txt")
    }
    assertThrows[InvalidMapError] {
      val map = new GameMap("test.invalid_map_char.txt")
    }
  }

  "Map" should "create correctly formatted map based on a file." in {
    val map = new GameMap("test.valid_map.txt")

    for (i <- 0 until map.map.length) {
      assert(map.map(i).length === MAP_WIDTH)
      for (b <- 0 until map.map(i).length) {
        // PathTile and BgTiles in a correct position (check the given file).
        if (b === 16) assert(map.map(i)(b).isInstanceOf[PathTile])
        else assert(map.map(i)(b).isInstanceOf[BgTile])
      }
    }
    assert(map.map.length === MAP_HEIGHT)
    // Check whether end and start points are correct.
    assert(map.startPoint.coord === (0,16))
    assert(map.endPoint.coord === (11,16))
  }
  
end MapTest