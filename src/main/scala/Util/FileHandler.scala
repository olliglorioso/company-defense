package Util

import scala.io.Source
import unpickle.default._

class FileHandler {
  def readLinesFromFile(filePath: String): Array[String] = {
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines().toArray
    source.close()
    lines
  }

  def writeLinesToJson(filePath: String, lines: Array[String]): Unit = {
  }

  def readLinesFromJson(filePath: String): Array[String] = {
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines().toArray
    source.close()
    lines
  }
}
