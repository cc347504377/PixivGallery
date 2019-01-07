package com.luoye.whr.gallery.view.preview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.luoye.whr.gallery.R
import com.luoye.whr.gallery.adapter.ViewPagerFrgAdapter
import com.luoye.whr.gallery.common.IllustsBean
import kotlinx.android.synthetic.main.activiy_full_img_pinch.*

class FullImgPinchActivity : AppCompatActivity() {

    companion object {
        var bean: IllustsBean? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_full_img_pinch)
        intViewPager()
    }

    private fun intViewPager() {
        val frgList = ArrayList<Fragment>()
        bean?.let { bean ->
            val metaPages = bean.metaPages
            if (metaPages.isNotEmpty()) {
                metaPages.forEach {
                    it.imageUrls.original
                    it.imageUrls.medium
                    frgList.add(FullImgPichFragment().apply {
                        arguments = Bundle().apply {
                            putString("url", it.imageUrls.medium)
                            putString("originUrl", it.imageUrls.original)
                            putString("pId", bean.id)
                        }
                    })
                }
            } else {
                frgList.add(FullImgPichFragment().apply {
                    arguments = Bundle().apply {
                        putString("url", bean.imageUrls.medium)
                        putString("originUrl", bean.metaSinglePage.originalImageUrl)
                        putString("pId", bean.id)
                    }
                })
            }
            vp_pinch.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(p0: Int) {

                }

            })
            vp_pinch.adapter = ViewPagerFrgAdapter(supportFragmentManager, frgList)
        }
    }

}