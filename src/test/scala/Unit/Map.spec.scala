import collection.mutable.Buffer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BufferTest extends AnyFlatSpec with Matchers:

  "Buffer" should "store every appended value as its last element." in {
    val myBuffer = Buffer[String]()
    assume(myBuffer.isEmpty)

    myBuffer += "Cat"
    assert("Cat" === myBuffer.last)
    myBuffer += "Dog"
    assertResult("Dog") {
        myBuffer.last
    }
  }

  it should "return None from lastOption when it is empty." in {
    assert(Buffer[String]().lastOption === None)
  }
end BufferTest