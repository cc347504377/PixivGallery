package com.luoye.whr.pixivGallery.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.luoye.whr.pixivGallery.MyApplication
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.pixivGallery.view.home.recomment.RecommendFragment
import com.luoye.whr.pixivGallery.view.base.BaseControlFragment
import com.luoye.whr.pixivGallery.view.home.follow.FollowFragment
import com.luoye.whr.pixivGallery.view.home.like.LikeFragment
import com.luoye.whr.pixivGallery.view.home.rank.RankFragment
import com.luoye.whr.pixivGallery.view.login.LoginActivity
import com.luoye.whr.pixivGallery.view.search.SearchActivity
import com.luoye.whr.kotlinlibrary.base.PermissionBaseActivity
import com.luoye.whr.kotlinlibrary.util.dpToPx
import com.luoye.whr.kotlinlibrary.util.loadImg
import com.luoye.whr.kotlinlibrary.util.startActivity
import com.luoye.whr.pixivGallery.view.home.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_mian_content.*

class HomeActivity : PermissionBaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override var permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE)
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var permissionRequested = false
    private lateinit var headView: ImageView
    private lateinit var nameView: TextView
    private val rankFragment by lazy { RankFragment() }
    private val recommendFragment by lazy { RecommendFragment() }
    private val likeFragment by lazy { LikeFragment() }
    private val followFragment by lazy { FollowFragment() }
    private val frgList = ArrayList<BaseControlFragment>()
    private var currentFrg: BaseControlFragment? = null
    private val tbShadow by lazy { dpToPx(3f, this) }

    override fun init() {
        permissionRequested = true
        setContentView(R.layout.activity_main)
        initShareListener()
        initView()
        initListener()
    }

    private val sharedElementsCallList = ArrayList<(names: MutableList<String>, sharedElements: MutableMap<String, View>) -> Unit>()

    fun addSharedCallback(
        callback: ((names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) -> Unit)
    ) {
        if (!sharedElementsCallList.contains(callback)) {
            sharedElementsCallList.add(callback)
        }
    }

    fun removeSharedCallback(
        callback: ((names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) -> Unit)
    ) {
        sharedElementsCallList.remove(callback)
    }

    /**
     * 激活共享元素回调
     */
    private fun initShareListener() {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
                sharedElementsCallList.forEach {
                    it.invoke(names, sharedElements)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home_search -> {
                startActivity<SearchActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initListener() {
        nav_view.setNavigationItemSelectedListener(this)
        headView.setOnClickListener {
        }
        fab_frag_change.setOnClickListener {
            currentFrg?.apply {
                styleFlag++
                changeStyle()
            }
        }
        bn_main.setOnNavigationItemSelectedListener { it ->
            tv_home_tb_title.text = it.title
            when (it.itemId) {
                R.id.nv_item_fire -> {
                    selectFragment(rankFragment)
                }
                R.id.nv_item_home -> {
                    selectFragment(recommendFragment)
                }
                R.id.nv_item_collect -> {
                    selectFragment(likeFragment)
                }
                R.id.nv_item_follow -> {
                    selectFragment(followFragment)
                }
            }
            true
        }
        bn_main.selectedItemId = R.id.nv_item_fire
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.drawer_menu_setting -> {
                startActivity<SettingActivity>()
            }
            R.id.drawer_menu_fresh -> {
                MyApplication.context.get()?.refreshToken()
            }
            R.id.drawer_menu_exit -> {
                SpUtil.auth = ""
                startActivity<LoginActivity>()
                finish()
            }
        }
        dl_main.closeDrawer(GravityCompat.START)
        return false
    }

    @SuppressLint("ResourceType")
    private fun initView() {
        // toolbar
        setSupportActionBar(tb_main)
        supportActionBar?.title = ""
        // 侧滑控制按钮
        drawerToggle = ActionBarDrawerToggle(this, dl_main, tb_main, R.string.drawer_open, R.string.drawer_close)
        dl_main.addDrawerListener(drawerToggle)
        // 底部导航栏颜色
        bn_main.itemIconTintList = resources.getColorStateList(R.drawable.selector_bottom_nativation_item)
        bn_main.itemTextColor = resources.getColorStateList(R.drawable.selector_bottom_nativation_item)
        // 初始化控件引用
        nav_view.getHeaderView(0).apply {
            headView = findViewById(R.id.iv_head_img)
            nameView = findViewById(R.id.tv_head_name)
        }
        nameView.text = SpUtil.userName
        loadImg(SpUtil.userHead, headView)
    }

    private fun selectFragment(fragment: BaseControlFragment) {
        if (currentFrg == fragment) {
            return
        }
        currentFrg = fragment
        if (!frgList.contains(fragment)) {
            fragmentOperation { transition ->
                frgList.forEach {
                    transition.hide(it)
                }
                frgList.add(fragment)
                transition.add(R.id.fl_fragment_layout, fragment)
            }
        } else {
            fragmentOperation { transition ->
                frgList.forEach {
                    transition.hide(it)
                }
                transition.show(fragment)
            }
        }
        // rank页面tabLayout自带阴影，不需要显示
        al_home.elevation = if (fragment is RankFragment) {
            0f
        } else {
            tbShadow
        }
    }

    /**
     * 模板化fragment管理
     */
    private inline fun fragmentOperation(operation: (FragmentTransaction) -> Unit) {
        val transaction = supportFragmentManager.beginTransaction()
        operation(transaction)
        transaction.commit()
    }

    /**
     * 配置drawerToggle与toolbar联动
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (permissionRequested) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (permissionRequested) {
            drawerToggle.syncState()
        }
    }
}
