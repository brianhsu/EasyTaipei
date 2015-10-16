package moe.brianhsu.easytaipei

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentManager
import android.view.Menu
import android.view.View
import android.support.design.widget.Snackbar
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener

import scala.collection.JavaConversions._

class MainActivity extends AppCompatActivity with TypedFindView
{
  private lazy val tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    val actionBar = findView(TR.toolbar)
    val viewPager = findView(TR.container)
    val tabLayout = findView(TR.tabs)
    val fab = findView(TR.fab)
    viewPager.setAdapter(tabPagerAdapter)
    viewPager.setOffscreenPageLimit(10)
    tabLayout.setupWithViewPager(viewPager)
    setSupportActionBar(actionBar)
    fab.setOnClickListener(new View.OnClickListener {
      override def onClick(view: View) {
        val fragments = getSupportFragmentManager().getFragments
        val visibleFragments = fragments.filter(f => f != null && f.getUserVisibleHint)
        visibleFragments.foreach { f => f.asInstanceOf[BaseFragment].updateMarkers() }
      }
    })
  }

  class TabPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {
    
    val tolietFragment = new TolietFragment
    val trashCanFragment = new TrashCanFragment
    val drinkingStationFragment = new DrinkingStationFragment
    val breastfeedingRoomFragment = new BreastfeedingRoomFragment

    override def getCount = 4
    override def getPageTitle(position: Int): CharSequence = position match {
      case 0 => "公共廁所"
      case 1 => "行人垃圾筒"
      case 2 => "飲水台"
      case 3 => "哺集乳室"
    }

    override def getItem(position: Int): Fragment = position match {
      case 0 => tolietFragment
      case 1 => trashCanFragment
      case 2 => drinkingStationFragment
      case 3 => breastfeedingRoomFragment
    }
  }
}



