

import collection.mutable.Buffer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Util.Constants._
import _root_.Logic._
import Util.FileHandler
import Util.HelperFunctions
import scalafx.geometry.Point2D
import scala.collection.mutable.ArrayBuffer

class LogicTests extends AnyFlatSpec with Matchers:

  // MAP TESTS

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

  // FILE HANDLER TESTS

  val fh = new FileHandler()

  "Filehandler" should "throw an exception if the file is not found." in {
    assertThrows[Exception] {
      fh.readLinesFromFile("/Maps/this_file_does_not_exist.txt")
    }
  }

  "Filehandler" should "throw an exception if the file is not a valid JSON file." in {
    assertThrows[Exception] {
      fh.readLinesFromJsonFile("/Maps/test.valid_map.txt") // is not a json file even
    }
  }

  "Filehandler" should "read txt file correctly." in {
    val lines = fh.readLinesFromFile("/Maps/test.valid_map.txt")
    assert(lines.length === 12)
    for (i <- 0 until lines.length - 1) {
      assert(lines(i).length === 20)
    }
    assert(lines(0) == "00000000000000002000")
    for (i <- 1 until lines.length - 1) {
      assert(lines(i) == "00000000000000001000")
    }
    assert(lines(lines.length - 1) == "00000000000000003000")
  }

  

end LogicTests
