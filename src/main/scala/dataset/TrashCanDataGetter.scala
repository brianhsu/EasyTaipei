package moe.brianhsu.easytaipei

import android.content.Context
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._

class TrashCanDataGetter(context: Context, dataSetID: String, cacheName: String) extends BaseDataGetter(context) {
  protected val jsonDataURL =  s"http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=$dataSetID" 
  protected val cacheFileName = cacheName

  def parseToDataList(jsonData: String): List[MarkerItem] = {
    val JArray(trashCans) = parse(jsonData) \\ "results"

    trashCans.map { case data => 
      val road = (data \ "路名").values.toString
      val section = (data \ "段、號及其他註明").values.toString
      new MarkerItem(
        title = s"$road / $section",
        lat = (data \ "緯度").values.toString.toDouble,
        lng = (data \ "經度").values.toString.toDouble
      )
    }
  }
}
