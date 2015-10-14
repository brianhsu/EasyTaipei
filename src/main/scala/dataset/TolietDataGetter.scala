package moe.brianhsu.easytaipei

import android.content.Context
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._

class TolietDataGetter(context: Context) extends BaseDataGetter[MarkerItem](context) {

  protected val jsonDataURL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=199a519c-5113-4e40-b00b-22a54ecf6d70"
  protected val cacheFileName = "/tolietDataSet.json"

  def parseToTolietList(jsonData: String): List[MarkerItem] = {
    val JArray(toliets) = parse(jsonData) \\ "results"

    toliets.map { case data => 
      new MarkerItem(
        title = (data \ "title").values.toString,
        lat = (data \ "lat").values.toString.toDouble,
        lng = (data \ "lng").values.toString.toDouble
      )
    }
   
  }
}

