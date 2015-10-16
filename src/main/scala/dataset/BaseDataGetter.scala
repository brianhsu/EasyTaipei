package moe.brianhsu.easytaipei

import android.content.Context
import android.os.Environment
import java.io._
import scala.io.Source
import scala.util.Try

abstract class BaseDataGetter(context: Context) {

  protected val cacheFileName: String
  protected val jsonDataURL: String
  protected def parseToDataList(jsonData: String): List[MarkerItem]

  def getCacheDir: String = {
    val hasSDCard = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable

    hasSDCard match {
      case true  => context.getExternalCacheDir().getPath()
      case false => context.getCacheDir().getPath()
    }  
  }

  def getJsonFromFile: Try[List[MarkerItem]] = Try { parseToDataList(Source.fromFile(s"${getCacheDir}/$cacheFileName").mkString) }
  def getJsonFromNetwork: Try[List[MarkerItem]] = Try {
    val jsonData = Source.fromURL(jsonDataURL).mkString
    val cacheFile = new PrintWriter(new File(s"${getCacheDir}/$cacheFileName"))
    cacheFile.println(jsonData)
    cacheFile.close()
    parseToDataList(jsonData)
  }

  def getJsonData(forceUpdate: Boolean): List[MarkerItem] = {
    forceUpdate match {
      case true  => getJsonFromNetwork.get
      case false => (getJsonFromFile orElse getJsonFromNetwork).get
    }
  }
}

