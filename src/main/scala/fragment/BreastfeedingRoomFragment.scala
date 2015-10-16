package moe.brianhsu.easytaipei

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.view.ClusterRenderer

class BreastfeedingRoomFragment extends BaseFragment {

  override protected def getRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager[MarkerItem]): ClusterRenderer[MarkerItem] = {
    new DefaultClusterRenderer(context, map, clusterManager) {
      override def onBeforeClusterItemRendered(item: MarkerItem, markerOptions: MarkerOptions): Unit = {
        markerOptions.title(item.title)
        item.snippet.foreach(markerOptions snippet _)
      }
    }
  }

  protected def getDataSet: Future[List[MarkerItem]] = Future { 
    val dataGetter = new BreastfeedingRoomDataGetter(getContext)
    dataGetter.getJsonData 
  }

}
