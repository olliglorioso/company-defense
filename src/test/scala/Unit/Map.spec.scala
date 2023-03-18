import collection.mutable.Buffer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Logic.GameMap

class MapTest extends AnyFlatSpec with Matchers:
  "Map" should "check if the text file for map is correctly formatted." in {
    val map = new GameMap("test.invalid_map.txt")
  }
  
end MapTest