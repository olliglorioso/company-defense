package Util

import scala.io.Source
import upickle.default._
import ujson._
import java.io.FileWriter
class FileHandler:
  def readLinesFromFile(filePath: String): Array[String] = 
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines().toArray
    source.close()
    lines
  end readLinesFromFile

  /**
   * Reads a JSON-file and returns JSON-like object.
   * @param filePath the path to the file
   * @return the contents of the file as ujson.Value
   */
  def readLinesFromJsonFile(filePath: String) = 
    val source = Source.fromFile("./src/main/scala/Resources/" + filePath)
    val lines = source.getLines()
    var jsonString = ""
    for line <- lines do
      jsonString += line
    val data = ujson.read(jsonString)
    source.close()
    data
  end readLinesFromJsonFile

  def writeLinesToJsonFile(filePath: String, ujsonObj: Value) = // use uPickle
    val toWrite = readLinesFromFile(filePath)
    val source = FileWriter("./src/main/scala/Resources/" + filePath)
    source.write(upickle.default.write(ujsonObj))
    source.close()
  end writeLinesToJsonFile

end FileHandler

@main def koira () =
  val data = FileHandler().readLinesFromJsonFile("Saved/LATEST_saved.json")
  println(FileHandler().writeLinesToJsonFile("Saved/LATEST_saved.json", data))