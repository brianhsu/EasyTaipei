package moe.brianhsu.easytaipei

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.view.ClusterRenderer

class TolietFragment extends BaseFragment {


  protected def getDataSet(forceUpdate: Boolean): Future[List[MarkerItem]] = Future { 
    val tolietDataGetter = new TolietDataGetter(getContext)
    tolietDataGetter.getJsonData(forceUpdate)
  }

}
