package com.luoye.whr.gallery.view.home.rank

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bigkoo.pickerview.TimePickerView
import com.luoye.whr.gallery.R
import com.luoye.whr.gallery.adapter.HomeFrgAdapter
import com.luoye.whr.gallery.view.base.BaseControlFragment
import kotlinx.android.synthetic.main.fragment_rank.*
import java.text.SimpleDateFormat
import java.util.*

class RankFragment : BaseControlFragment() {
    private val frgList = ArrayList<RankListFragment>()
    private val tabList = listOf("日榜", "周榜", "月榜", "新人", "原创", "男性向", "女性向", "成人向")
    private var selectDate: String = ""
    private var homeFrgAdapter: HomeFrgAdapter? = null

    private val piker by lazy {
        //P站有效数据起始时间 2007.10.1
        val startDate = Calendar.getInstance().apply {
            set(2007, 9, 1)
        }
        //当天时间没有数据
        val endDate = Calendar.getInstance().apply {
            time = Date(System.currentTimeMillis()-24*60*60*1000)
        }
        TimePickerView.Builder(requireContext()) { date, v ->
            selectDate = SimpleDateFormat("yyyy-MM-dd").format(date)
            initFrg()
            initViewpager()
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
        bt_menu.setOnClickListener {
            piker.show()
        }
        initFrg()
        initViewpager()
    }

    private fun initViewpager() {
        homeFrgAdapter = HomeFrgAdapter(requireFragmentManager(), frgList, tabList)
        vp_rank_fragment.offscreenPageLimit = frgList.size
        vp_rank_fragment.adapter = homeFrgAdapter
        tl_rank_fragment.setupWithViewPager(vp_rank_fragment, false)
    }

    private fun initFrg() {
        requireFragmentManager().beginTransaction().apply {
            frgList.forEach {
                remove(it)
                it.onDestroyView()
            }
            frgList.clear()
            commit()
        }
        for (i in 0 until tabList.size) {
            frgList.add(RankListFragment().apply {
                arguments = Bundle().apply {
                    putInt("mode", i)
                    putString("date", selectDate)
                }
            })
        }
    }

    override fun changeStyle() {
        frgList.forEach {
            it.changeStyle()
        }
    }

}