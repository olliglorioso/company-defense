package Util

import scala.io.Source

class FileHandler {
  def readLinesFromFile(filePath: String): Array[String] = {
    val source = Source.fromFile("./src/resources/" + filePath)
    val lines = source.getLines().toArray
    source.close()
    lines
  }
}
