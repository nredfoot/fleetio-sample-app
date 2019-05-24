package com.redfootapps.nickredfoot.fleetio.sample.app.ui.tab

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.redfootapps.nickredfoot.fleetio.sample.app.R
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.list.ListFragment
import com.redfootapps.nickredfoot.fleetio.sample.app.ui.map.MapFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)  {

    // FragmentPagerAdapter

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> ListFragment.newInstance()
            else ->  MapFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}