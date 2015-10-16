package moe.brianhsu.easytaipei

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import scala.concurrent.Future
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.view.ClusterRenderer
import scala.concurrent.ExecutionContext

import java.util.concurrent.Executors

class TrashCanFragment extends BaseFragment {

  implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(12))

  protected def getDataSet(forceUpdate: Boolean): Future[List[MarkerItem]] = {
    
    val datasets = Array(
      Future { new TrashCanDataGetter(getContext, "97cc923a-e9ee-4adc-8c3d-335567dc15d3", "area01.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "5fa14e06-018b-4851-8316-1ff324384f79", "area02.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "f40cd66c-afba-4409-9289-e677b6b8d00e", "area03.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "33b2c4c5-9870-4ee9-b280-a3a297c56a22", "area04.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "0b544701-fb47-4fa9-90f1-15b1987da0f5", "area05.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "37eac6d1-6569-43c9-9fcf-fc676417c2cd", "area06.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "46647394-d47f-4a4d-b0f0-14a60ac2aade", "area07.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "05d67de9-a034-4177-9f53-10d6f79e02cf", "area08.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "179d0fe1-ef31-4775-b9f0-c17b3adf0fbc", "area09.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "8cbb344b-83d2-4176-9abd-d84508e7dc73", "area10.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "7b955414-f460-4472-b1a8-44819f74dc86", "area11.txt").getJsonData(forceUpdate) },
      Future { new TrashCanDataGetter(getContext, "5697d81f-7c9d-43fc-a202-ae8804bbd34b", "area12.txt").getJsonData(forceUpdate) }
    )

    for {
      dataOfArea01 <- datasets(0)  recover { case e: Exception => Nil }
      dataOfArea02 <- datasets(1)  recover { case e: Exception => Nil }
      dataOfArea03 <- datasets(2)  recover { case e: Exception => Nil }
      dataOfArea04 <- datasets(3)  recover { case e: Exception => Nil }
      dataOfArea05 <- datasets(4)  recover { case e: Exception => Nil }
      dataOfArea06 <- datasets(5)  recover { case e: Exception => Nil }
      dataOfArea07 <- datasets(6)  recover { case e: Exception => Nil }
      dataOfArea08 <- datasets(7)  recover { case e: Exception => Nil }
      dataOfArea09 <- datasets(8)  recover { case e: Exception => Nil }
      dataOfArea10 <- datasets(9)  recover { case e: Exception => Nil }
      dataOfArea11 <- datasets(10) recover { case e: Exception => Nil }
      dataOfArea12 <- datasets(11) recover { case e: Exception => Nil }
    } yield {
      dataOfArea01 ++ dataOfArea02 ++
      dataOfArea03 ++ dataOfArea04 ++
      dataOfArea05 ++ dataOfArea06 ++
      dataOfArea07 ++ dataOfArea08 ++
      dataOfArea09 ++ dataOfArea10 ++
      dataOfArea11 ++ dataOfArea12
    }
  }

}
