package moe.brianhsu.easytaipei

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

case class MarkerItem(title: String, lat: Double, lng: Double) extends ClusterItem {
  val position = new LatLng(lat, lng)
  override def getPosition = position
}

