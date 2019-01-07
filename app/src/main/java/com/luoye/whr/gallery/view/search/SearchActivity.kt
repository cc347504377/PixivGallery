package com.luoye.whr.gallery.view.search

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import com.luoye.whr.gallery.R
import kotlinx.android.synthetic.main.activity_search.*
import android.widget.ArrayAdapter
import com.luoye.whr.gallery.adapter.SearchTagAdapter
import com.luoye.whr.gallery.common.RecommendTagBean
import com.luoye.whr.gallery.common.SearchAutocompleteBean
import com.luoye.whr.gallery.common.SpUtil
import com.luoye.whr.gallery.presenter.PixivImagePresenter
import com.google.gson.Gson
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import org.jetbrains.anko.doAsync


class SearchActivity : AppCompatActivity() {

    private var lastSearch = ""
    private lateinit var adapter: SearchTagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()
        initSearchEdit()
        loadView()
    }

    private fun loadView() {
        //相隔时间间隔大于一天刷新一次数据
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - SpUtil.searchBeanTime >= 24 * 60 * 60 * 1000) {
            loadViewFromNet()
        } else {
            loadViewFromSp()
        }
    }

    private fun loadViewFromSp() {
        doAsync {
            val bean = Gson().fromJson(SpUtil.searchBean, RecommendTagBean::class.java).trendTags.toMutableList()
            initList(bean)
        }
    }

    private fun loadViewFromNet() {
        clPb_search_tag_recommend.visibility = View.VISIBLE
        clPb_search_tag_recommend.show()
        PixivImagePresenter.getIllustTrendTags(object : PublicCallback.DataCallBack<String> {
            override fun onSuccess(t: String) {
                doAsync {
                    SpUtil.searchBeanTime = System.currentTimeMillis()
                    SpUtil.searchBean = t
                    val bean = Gson().fromJson(t, RecommendTagBean::class.java).trendTags.toMutableList()
                    initList(bean)
                }
            }

            override fun onStart() {
            }

            override fun onFailed(msg: String) {
            }
        })
    }

    private fun initList(data: MutableList<RecommendTagBean.TrendTag>) {
        adapter = SearchTagAdapter(this@SearchActivity, data)
        runOnUiThread {
            clPb_search_tag_recommend.hide()
            rv_search_tag_recommend.layoutManager = GridLayoutManager(this@SearchActivity, 3)
            rv_search_tag_recommend.adapter = adapter
        }
    }

    private fun initSearchEdit() {
        iv_search_ic.visibility = View.INVISIBLE
        //动态补全推荐tag
        ac_search_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = ac_search_input.text.toString()
                if (lastSearch != input) {
                    if (input.isNotBlank()) {
                        iv_search_ic.visibility = View.VISIBLE
                    } else {
                        iv_search_ic.visibility = View.INVISIBLE
                    }
                    PixivImagePresenter.getSearchAutoCompleteKeywords(ac_search_input.text.toString(), object : PublicCallback.DataCallBack<SearchAutocompleteBean> {
                        override fun onSuccess(t: SearchAutocompleteBean) {
                            autoComplete(t.search_auto_complete_keywords!!)
                        }

                        override fun onStart() {
                        }

                        override fun onFailed(msg: String) {
                        }
                    })
                    lastSearch = input
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        //点击补全结果
        ac_search_input.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            lastSearch = ac_search_input.text.toString()
            startSearch(lastSearch)
        }
        //软键盘搜索
        ac_search_input.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startSearch(ac_search_input.text.toString())
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (inputMethodManager.isActive) {
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                }
            }
            true
        }
        //清空按钮
        iv_search_ic.setOnClickListener {
            ac_search_input.setText("")
        }
    }

    private fun initToolbar() {
        setSupportActionBar(tb_search)
        supportActionBar?.title = "搜索"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb_search.setNavigationOnClickListener {
            finish()
        }
    }

    private fun startSearch(key: String) {
        startActivity(Intent(this, SearchListActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString("keyWord", key)
            })
        })
    }

    private fun autoComplete(data: List<String>) {
        if (data.size == 1 && data[0] == lastSearch) {
            return
        }
        val adapter = ArrayAdapter<String>(this,
                R.layout.item_autocomplete, data)
        ac_search_input.setAdapter(adapter)
        ac_search_input.showDropDown()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        ac_search_input.hint = menu.findItem(R.id.menu_home_search1).title
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        ac_search_input.hint = item.title
        when (item.itemId) {
            R.id.menu_home_search1 -> {
            }
            R.id.menu_home_search2 -> {
            }
            R.id.menu_home_search3 -> {
            }
            R.id.menu_home_search4 -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
