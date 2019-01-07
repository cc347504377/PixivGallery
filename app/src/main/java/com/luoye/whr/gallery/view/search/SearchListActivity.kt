package com.luoye.whr.gallery.view.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.luoye.whr.gallery.R
import kotlinx.android.synthetic.main.activity_search_list.*

class SearchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_list)
        initToolbar()
        initFrg()
    }

    private fun initFrg() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = SearchFragment()
        transaction.add(R.id.fl_search_layout, fragment)
        fragment.arguments = intent.extras
        transaction.commit()
    }

    private fun initToolbar() {
        setSupportActionBar(tb_search_list)
        supportActionBar?.title = intent.extras?.getString("keyWord")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb_search_list.setNavigationOnClickListener {
            finish()
        }
    }
}
