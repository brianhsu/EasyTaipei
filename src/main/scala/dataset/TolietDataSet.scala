package moe.brianhsu.easytaipei

import android.content.Context
import android.os.Environment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._
import scala.io.Source
import scala.util.Try

import com.google.maps.android.clustering.ClusterItem
import com.google.android.gms.maps.model.LatLng


case class TolietData(title: String, lat: Double, lng: Double) extends ClusterItem {
  val position = new LatLng(lat, lng)
  override def getPosition = position
}

class TolietDataSet(context: Context) {

  def getCacheDir: String = {
    val hasSDCard = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable

    hasSDCard match {
      case true  => context.getExternalCacheDir().getPath()
      case false => context.getCacheDir().getPath()
    }  
  }

  def getJsonFromFile: Try[String] = Try { 
    val q = Source.fromFile(s"${getCacheDir}/tolietDataSet.json").mkString 
    println("=====> q:" + q)
    q
  }

  def getJsonFromNetwork: Try[String] = Try {
    val jsonURL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=199a519c-5113-4e40-b00b-22a54ecf6d70"
    val jsonData = Source.fromURL(jsonURL).mkString
    val cacheFile = new PrintWriter(new File(s"${getCacheDir}/tolietDataSet.json"))
    cacheFile.close()
    println("======> jsonData:" + jsonData)
    jsonData
  }

  def parseToTolietList(jsonData: String): List[TolietData] = {
    println("=====> parseToTolietList")
    val JArray(toliets) = parse(jsonData) \\ "results"

    toliets.map { case data => 
      new TolietData(
        title = (data \ "title").values.toString,
        lat = (data \ "lat").values.toString.toDouble,
        lng = (data \ "lng").values.toString.toDouble
      )
    }
   
  }

  def getJsonData: List[TolietData] = {
    println("====> getJsonData...")
    val jsonDataHolder = getJsonFromFile.map(parseToTolietList) orElse getJsonFromNetwork.map(parseToTolietList)
    println("====> getJsonData reslt:" + jsonDataHolder)
    jsonDataHolder.get
  }


}

