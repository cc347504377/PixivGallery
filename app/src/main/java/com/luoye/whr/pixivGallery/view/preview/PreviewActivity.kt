package com.luoye.whr.pixivGallery.view.preview

import android.app.ActivityOptions
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.adapter.ViewPagerFrgAdapter
import com.luoye.whr.pixivGallery.common.IllustsBean
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {

    private val position by lazy {
        intent.getIntExtra("position", 0)
    }

    private val shareElementStat by lazy {
        intent.getBooleanExtra("shareElement", false)
    }

    companion object {
        var data: List<IllustsBean>? = null
        private val selectListenerList = ArrayList<(Int) -> Unit>()
        /**
         * viewPager对外回调
         */
        fun addSelectedListener(onSelected: (Int) -> Unit) {
            selectListenerList.add(onSelected)
        }

        fun startActivity(
            fragment: Fragment,
            data: List<IllustsBean>,
            position: Int,
            shareView: View? = null
        ) {
            val intent = Intent().apply {
                PreviewActivity.data = data
                setClass(fragment.requireContext(), PreviewActivity::class.java)
                putExtra("position", position)
            }
            if (shareView == null) {
                fragment.startActivity(intent)
            } else {
                intent.putExtra("shareElement", true)
                val bundle = ActivityOptions.makeSceneTransitionAnimation(fragment.requireActivity(),
                    shareView, fragment.getString(R.string.transName)).toBundle()
                fragment.startActivity(intent, bundle)
            }
        }
    }

    private val frgList = ArrayList<PreViewFragment>()
    private var selectIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        initShared()
        initFrag()
        initListener()
    }

    private fun initShared() {
        if (!shareElementStat) {
            return
        }
        supportPostponeEnterTransition()
        selectIndex = position
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                names?.clear()
                sharedElements?.clear()
                sharedElements?.put(getString(R.string.transName), frgList[selectIndex].getTransView())
            }
        })
    }

    private fun initListener() {
        vp_preview.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                selectIndex = p0
                selectListenerList.forEach {
                    it.invoke(p0)
                }
            }
        })
    }

    private fun initFrag() {
        var currentPosition = 0
        data?.forEach {
            frgList.add(PreViewFragment().apply {
                bean = it
                if (currentPosition == position) {
                    current = true
                }
            })
            currentPosition++
        }
        vp_preview.offscreenPageLimit = 1
        vp_preview.adapter = ViewPagerFrgAdapter(supportFragmentManager, frgList)
        vp_preview.currentItem = position
    }

    override fun onBackPressed() {
        selectListenerList.forEach {
            it.invoke(selectIndex)
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        selectListenerList.clear()
        super.onDestroy()
    }
}
