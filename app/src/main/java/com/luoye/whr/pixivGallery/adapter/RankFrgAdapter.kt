package com.luoye.whr.pixivGallery.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

class RankFrgAdapter(fm: FragmentManager, private val frgList: List<Fragment>, private val titleList: List<CharSequence>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment = frgList.get(p0)

    override fun getCount(): Int = frgList.size

    override fun getPageTitle(position: Int) = titleList[position]

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}