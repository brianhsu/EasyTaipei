package moe.brianhsu.easytaipei

import android.content.Context
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._

class DrinkingStationDataGetter(context: Context) extends BaseDataGetter[MarkerItem](context) {

  protected val jsonDataURL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=2924f7f6-221e-416e-93ff-e8561e8d7327"
  protected val cacheFileName = "/drinkingStation.json"

  def parseToTolietList(jsonData: String): List[MarkerItem] = {
    val JArray(toliets) = parse(jsonData) \\ "results"

    toliets.map { case data => 
      val title1 = (data \ "場所次分類").values.toString 
      val title2 = (data \ "場所名稱").values.toString
      val title3 = (data \ "設置地點").values.toString

      new MarkerItem(
        title = s"$title1 / $title2 / $title3",
        lat = (data \ "緯度").values.toString.toDouble,
        lng = (data \ "經度").values.toString.toDouble
      )
    }
   
  }
}

