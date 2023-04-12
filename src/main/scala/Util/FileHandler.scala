package Util

import scala.io.Source
import upickle.default._
import ujson._
class FileHandler:
  def readLinesFromFile(filePath: String): Array[String] = 
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines().toArray
    source.close()
    lines
  end readLinesFromFile

  def readLinesFromJsonFile(filePath: String): Value = 
    // read to a string from filepath
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines()
    var jsonString = ""
    for line <- lines do
      jsonString += line
    val data = ujson.read(jsonString)
    source.close()
    data
  end readLinesFromJsonFile

end FileHandler

@main def koira () =
  println(FileHandler().readLinesFromJsonFile("Saved/saved_test.json"))