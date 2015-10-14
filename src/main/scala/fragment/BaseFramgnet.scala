package moe.brianhsu.easytaipei

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import TypedResource._

abstract class BaseFragment extends Fragment {

  private var mapView: MapView = _

  protected def getDataSet: Future[List[MarkerItem]]
  protected def getRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager[MarkerItem]): ClusterRenderer[MarkerItem]

  private def addMarkers(rootView: View, clusterManager: ClusterManager[MarkerItem]): Unit = {

    val loadingIndicator = Snackbar.make(rootView, "讀取中，請稍候", Snackbar.LENGTH_INDEFINITE)
    val dataSet = getDataSet

    loadingIndicator.show()

    dataSet.onSuccess { case dataList => 
      dataList.foreach(item => clusterManager.addItem(item))
      getActivity.runOnUiThread(new Thread {
        override def run() {
          clusterManager.cluster()
          loadingIndicator.dismiss()
        }
      })
    }

    dataSet.onFailure { case e: Exception =>
      e.printStackTrace()
      getActivity.runOnUiThread(new Thread {
        override def run() {
          loadingIndicator.dismiss()
          val snackBar = Snackbar.make(rootView, "無法取得公廁資料，請確認網路狀態", Snackbar.LENGTH_INDEFINITE)
          val actionCallback = new View.OnClickListener {
            override def onClick(view: View) {
              snackBar.dismiss()
              addMarkers(rootView, clusterManager)
            }
          }
          snackBar.setAction("重試", actionCallback)
          snackBar.setActionTextColor(android.graphics.Color.YELLOW)
          snackBar.show()
        }
      })
    }
  }

  private def createMapReadyCallback(rootView: View) = new OnMapReadyCallback with LocationListener {

    private var googleMap: GoogleMap = _
    private var hasFineLocation: Boolean = false

    private def updateDefaultLocation() {
      val locationManagerHolder = Option(getActivity.getSystemService(Context.LOCATION_SERVICE)).map(_.asInstanceOf[LocationManager])
      locationManagerHolder.foreach { locationManager =>
        val gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if(gpsIsEnabled) {
          locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        }

        if(networkIsEnabled) {
          locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }
      }
    }
    
    override def onLocationChanged(location: Location) {
      if (!hasFineLocation) {
        val cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude, location.getLongitude)).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (location.getProvider == LocationManager.GPS_PROVIDER) {
          hasFineLocation = true
        }
      }
    }

    override def onProviderDisabled(provider: String) {}
    override def onProviderEnabled(provider: String) {}
    override def onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override def onMapReady(googleMap: GoogleMap) {
      this.googleMap = googleMap
      googleMap.setMyLocationEnabled(true)
      val cameraPosition = new CameraPosition.Builder().target(new LatLng(25.041675, 121.551623)).zoom(11).build();
      googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      updateDefaultLocation()
      val clusterManager = new ClusterManager[MarkerItem](getContext, googleMap)
      googleMap.setOnCameraChangeListener(clusterManager)
      googleMap.setOnMarkerClickListener(clusterManager)
      clusterManager.setRenderer(getRenderer(getContext, googleMap, clusterManager))
      addMarkers(rootView, clusterManager)
    }
  }


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val rootView = inflater.inflate(R.layout.fragment_map, container, false)
    mapView = rootView.findView(TR.mapView)
    mapView.onCreate(savedInstanceState)
    mapView.onResume()

    MapsInitializer.initialize(getActivity().getApplicationContext())

    val mapReadyCallback = createMapReadyCallback(rootView)
    mapView.getMapAsync(mapReadyCallback)
    rootView
  }

  override def onResume() {
    super.onResume()
    mapView.onResume()
  }

  override def onPause() {
    super.onPause()
    mapView.onPause()
  }

  override def onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override def onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }
}

