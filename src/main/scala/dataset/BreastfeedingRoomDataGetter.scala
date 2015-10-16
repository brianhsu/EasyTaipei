package moe.brianhsu.easytaipei

import android.content.Context
import java.io._
import org.json4s._
import org.json4s.native.JsonMethods._
import android.location.Geocoder
import scala.util.Try
import java.io._
import scala.io.Source

class BreastfeedingRoomDataGetter(context: Context) extends BaseDataGetter(context) {

  protected val jsonDataURL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=5941f46b-4115-49b9-a511-18ea5b427676"
  protected val cacheFileName = "/breastfeedingRoom.json"
  

  private val coder = new Geocoder(context)
  private val locationCacheFile = getCacheDir + "/location.txt"
  private val locationCacheWriter = new PrintWriter(new FileWriter(locationCacheFile, true))

  private lazy val locationCache: Map[String, (Double, Double)] = {
    var cache: Map[String, (Double, Double)] = Map.empty
    Source.fromFile(locationCacheFile).getLines.foreach { line =>
      Try {
        val Array(address, latitude, longitude) = line.split(" ")
        val newLine = address -> (latitude.toDouble, longitude.toDouble)
        cache += newLine
      }
    }
    cache
  }

  def getAddressLocationFromNetwork(address: String): Try[(Double, Double)] = Try {
    val addressWithLocation = coder.getFromLocationName(address, 1).get(0)
    val latitude  = if (addressWithLocation.hasLatitude) addressWithLocation.getLatitude else -1
    val longitude = if (addressWithLocation.hasLongitude) addressWithLocation.getLongitude else - 1

    locationCacheWriter.println(s"$address $latitude $longitude")
    locationCacheWriter.flush()

    (latitude, longitude)
  }

  def getAddressLocation(address: String): Option[(Double, Double)] = {
    val locationFromCache = locationCache.get(address)
    locationFromCache orElse getAddressLocationFromNetwork(address).toOption
  }

  def parseToDataList(jsonData: String): List[MarkerItem] = {
    val JArray(rooms) = parse(jsonData) \\ "results"

    val resultDataList = rooms.map { case data => 

      val name = (data \ "name").values.toString
      val time = (data \ "content").values.toString
      val poiAddr = (data \ "poi_addr").values.toString
      val (latitude, longitude) = getAddressLocation(poiAddr) getOrElse (-1D, -1D)

      new MarkerItem(
        title = s"$name / $poiAddr",
        lat = latitude,
        lng = longitude,
        snippet = Some(time)
      )
    }

    resultDataList.filter(x => x.lat != -1 && x.lng != -1)
   
  }
}

