package Util

import scala.io.Source

class FileHandler:
  def readLinesFromFile(filePath: String): Array[String] = 
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines().toArray
    source.close()
    lines
  end readLinesFromFile

  def writeLinesToJson(filePath: String, lines: Array[String]): Unit = ???
  end writeLinesToJson

  def readLinesFromJson(filePath: String): Array[String] = 
    ???
  end readLinesFromJson
