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


class MainActivity extends AppCompatActivity with TypedFindView
{
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    val actionBar = findView(TR.toolbar)
    val viewPager = findView(TR.container)
    val tabLayout = findView(TR.tabs)
    val tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager)

    viewPager.setAdapter(tabPagerAdapter)
    tabLayout.setupWithViewPager(viewPager)
    setSupportActionBar(actionBar)

  }

  class TabPagerAdapter(fm: FragmentManager) extends FragmentPagerAdapter(fm) {
    override def getCount = 3
    override def getPageTitle(position: Int): CharSequence = position match {
      case 0 => "公共廁所"
      case 1 => "行人垃圾筒"
      case 2 => "飲水台"
    }

    override def getItem(position: Int): Fragment = position match {
      case 0 => new TolietFragment
      case 1 => new TrashCanFragment
      case 2 => new DrinkingFragment
    }
    
  }

  class DrinkingFragment extends Fragment {
    import TypedResource._
    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val rootView = inflater.inflate(R.layout.fragment_drinking_station, container, false)
      val textView = rootView.findView(TR.section_label)
      textView.setText("飲水台")
      rootView
    }
  }

  class TrashCanFragment extends Fragment {
    import TypedResource._
    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val rootView = inflater.inflate(R.layout.fragment_drinking_station, container, false)
      val textView = rootView.findView(TR.section_label)
      textView.setText("垃圾筒")
      rootView
    }
  }

  class TolietFragment extends Fragment {
    import TypedResource._
    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val rootView = inflater.inflate(R.layout.fragment_drinking_station, container, false)
      val textView = rootView.findView(TR.section_label)
      textView.setText("洗手間")
      rootView
    }
  }


}
