package Unit

import collection.mutable.Buffer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Util.Constants._
import Logic._

class MapTest extends AnyFlatSpec with Matchers:

  val validMap = new GameMap("/Maps/test.valid_map.txt")

  "Map" should "return an exception if map file is incorrectly formatted." in {
    assertThrows[Exception] {
      validMap.initializeMap("/Maps/test.invalid_map.txt")
    }
    assertThrows[Exception] {
      validMap.initializeMap("/Maps/test.invalid_map_char.txt")
    }
    assertThrows[Exception] {
      validMap.initializeMap("/Maps/test.invalid_map_path.txt")
    }
  }

  "Map" should "create correctly formatted map based on a file." in {
    for (i <- 0 until validMap.map.length) {
      assert(validMap.map(i).length === MAP_WIDTH)
      for (b <- 0 until validMap.map(i).length) {
        // PathTile and BgTiles in a correct position (check the given file).
        if (b === 16) assert(validMap.map(i)(b).isInstanceOf[PathTile])
        else assert(validMap.map(i)(b).isInstanceOf[BgTile])
      }
    }
    assert(validMap.map.length === MAP_HEIGHT)
    // Check whether end and start points are correct.
    assert(validMap.startPoint.coord === (0, 16))
    assert(validMap.endPoint.coord === (11, 16))
  }

  "Map" should "should have a correctly generated path queue." in {
    var idx = 1
    val path = validMap.pathQueue.map((elem) => {
      assert(elem.coord === (idx, 16))
      idx += 1
    })
  }

end MapTest
