package moe.brianhsu.easytaipei

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.view.ClusterRenderer

class TolietFragment extends BaseFragment[TolietData] {

  protected def getRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager[TolietData]): ClusterRenderer[TolietData] = {
    new DefaultClusterRenderer(context, map, clusterManager) {
      override def onBeforeClusterItemRendered(item: TolietData, markerOptions: MarkerOptions): Unit = {
        markerOptions.title(item.title)
      }
    }
  }

  protected def getDataSet: Future[List[TolietData]] = Future { 
    val tolietDataGetter = new TolietDataSet(getContext)
    tolietDataGetter.getJsonData 
  }

}
