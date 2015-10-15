package moe.brianhsu.easytaipei

import android.content.Context
import android.os.Environment
import java.io._
import scala.io.Source
import scala.util.Try

abstract class BaseDataGetter[T](context: Context) {

  protected val cacheFileName: String
  protected val jsonDataURL: String
  protected def parseToTolietList(jsonData: String): List[T]

  def getCacheDir: String = {
    val hasSDCard = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable

    hasSDCard match {
      case true  => context.getExternalCacheDir().getPath()
      case false => context.getCacheDir().getPath()
    }  
  }

  def getJsonFromFile: Try[String] = Try { Source.fromFile(s"${getCacheDir}/$cacheFileName").mkString }
  def getJsonFromNetwork: Try[String] = Try {
    val jsonData = Source.fromURL(jsonDataURL).mkString
    val cacheFile = new PrintWriter(new File(s"${getCacheDir}/$cacheFileName"))
    cacheFile.println(jsonData)
    cacheFile.close()
    jsonData
  }

  def getJsonData: List[T] = {
    val jsonDataFromCache = getJsonFromFile.map(parseToTolietList)
    println("=====> jsonDataFromCache:" + jsonDataFromCache)
    val jsonDataHolder = jsonDataFromCache orElse getJsonFromNetwork.map(parseToTolietList)
    jsonDataHolder.get
  }
}

