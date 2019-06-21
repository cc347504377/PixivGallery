package com.luoye.whr.pixivGallery.view.home.rank

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bigkoo.pickerview.TimePickerView
import com.luoye.whr.kotlinlibrary.util.log
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.adapter.RankFrgAdapter
import com.luoye.whr.pixivGallery.common.ModeEvent
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.pixivGallery.model.tabAdultData
import com.luoye.whr.pixivGallery.model.tabData
import com.luoye.whr.pixivGallery.view.base.BaseControlFragment
import kotlinx.android.synthetic.main.fragment_rank.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 排行模块父fragment，管理子tab列表
 */
class RankFragment : BaseControlFragment() {

    private val frgList = ArrayList<RankTabListFragment>()
    private val tabList = ArrayList<String>()
    private var selectDate: String = ""
    private var rankFrgAdapter: RankFrgAdapter? = null
    private var modeChanged = false

    private val piker by lazy {
        // P站有效数据起始时间 2007.10.1
        val startDate = Calendar.getInstance().apply {
            set(2007, 9, 1)
        }
        // 当天时间没有数据
        val endDate = Calendar.getInstance().apply {
            time = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
        }
        TimePickerView.Builder(requireContext()) { date, v ->
            selectDate = SimpleDateFormat("yyyy-MM-dd").format(date)
            initContent()
        }.setType(booleanArrayOf(true, true, true, false, false, false))
                .setRangDate(startDate, endDate)
                .setTitleText("FilterDate")
                .setDate(endDate)
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)
                .setSubmitText("Ok")
                .setCancelText("Cancel")
                .setLabel("", "", "", "", "", "")
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initListener()
        initContent()
    }

    private fun initListener() {
        bt_menu.setOnClickListener {
            piker.show()
        }
    }

    private fun initContent() {
        initTabData()
        initFrg()
        initViewpager()
    }

    private fun initTabData() {
        tabList.clear()
        tabList.addAll(if (SpUtil.adultMode) {
            tabAdultData
        } else {
            tabData
        })
    }

    private fun initFrg() {
        frgList.clear()
        for (i in 0 until tabList.size) {
            frgList.add(RankTabListFragment().apply {
                arguments = Bundle().apply {
                    putInt("mode", i)
                    putString("date", selectDate)
                }
            })
        }
    }

    private fun initViewpager() {
        if (rankFrgAdapter == null) {
            rankFrgAdapter = RankFrgAdapter(childFragmentManager, frgList, tabList)
            vp_rank_fragment.adapter = rankFrgAdapter
        } else {
            rankFrgAdapter?.notifyDataSetChanged()
        }
        vp_rank_fragment.offscreenPageLimit = frgList.size
        tl_rank_fragment.setupWithViewPager(vp_rank_fragment, false)
    }

    override fun changeStyle() {
        styleFlag++
        frgList.forEach {
            it.styleFlag = styleFlag
            it.changeStyle()
        }
    }

    override fun onResume() {
        super.onResume()
        if (modeChanged) {
            initContent()
            modeChanged = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onModeChanged(event: ModeEvent) {
        modeChanged = true
    }
}