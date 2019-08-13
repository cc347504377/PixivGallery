package com.luoye.whr.pixivGallery.view.preview

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.luoye.whr.kotlinlibrary.util.compressBitmap
import com.luoye.whr.kotlinlibrary.util.fileName
import com.luoye.whr.kotlinlibrary.util.runOnUiThread
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.common.folder
import com.luoye.whr.pixivGallery.common.loadPixvImg
import com.luoye.whr.pixivGallery.view.search.SearchListActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_img_preview.*
import org.jetbrains.anko.doAsync
import java.io.File

class PreViewFragment : Fragment() {

    var bean: IllustsBean? = null

    /**
     * 共享元素view
     */
    fun getTransView() = iv_fragment_preview!!

    // 是否为viewpager显示目标
    var current = false
    private val requestCode = 0x11

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_img_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        fl_fragment_preview_related.visibility = View.GONE
        bean?.let {
            initImage(it)
            initData(it)
            initTag(it)
//            initRelatedList()
        }
    }

    private fun initImage(bean: IllustsBean) {
        // 设置imageView比例
        iv_fragment_preview.apply {
            post {
                layoutParams = layoutParams.apply {
                    val ratio = bean.height / bean.width.toFloat()
                    height = (measuredWidth * ratio).toInt()
                }
                loadPreviewImage(bean)
            }
        }
    }

    private fun initData(bean: IllustsBean) {
        // 设置title
        tv_frg_pView_title.text = bean.title
        tv_frg_pView_time.text = bean.createDate.substringBefore('T')
        tv_frg_pView_views.text = bean.totalView.toString()
        tv_frg_pView_collection.text = bean.totalBookmarks.toString()
        // 显示图片数
        showImgSize(tv_fragment_preview_size, bean)
    }

    private fun initTag(bean: IllustsBean) {
        // 显示tag
        val tagList = ArrayList<String>()
        bean.tags.forEach {
            tagList.add(it.name)
        }
        fl_fragment_preview_tag.adapter = object : TagAdapter<String>(tagList) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val view = layoutInflater.inflate(R.layout.item_flow_tag, parent, false) as TextView
                view.text = tagList[position]
                return view
            }
        }
        // 跳转tag搜索
        fl_fragment_preview_tag.setOnTagClickListener { _, position, _ ->
            startActivity(Intent(activity, SearchListActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString("keyWord", tagList[position])
                })
            })
            true
        }
    }

    private fun initListener() {
        // 跳转大图预览
        iv_fragment_preview.setOnClickListener {
            startActivityForResult(Intent(requireContext(), FullImgPinchActivity::class.java), requestCode)
            FullImgPinchActivity.bean = bean
        }
        // 返回
        iv_frg_pView_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    /**
     * 加载预览图片
     * 当已缓存原图直接从文件加载
     * 如没有则从网络加载缩略图
     */
    private fun loadPreviewImage(it: IllustsBean) {
        if (!loadPreviewFromFile(it)) {
            requireActivity().loadPixvImg(it.imageUrls.medium, it.id, iv_fragment_preview, false,
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        shareElementLoad()
                        return false
                    }
                })
        }
    }

    /**
     * 从文件中加载原图
     */
    private fun loadPreviewFromFile(it: IllustsBean): Boolean {
        val metaPages = it.metaPages
        val originUrl = if (metaPages.isNotEmpty()) {
            metaPages[0].imageUrls.original
        } else {
            it.metaSinglePage.originalImageUrl
        }
        val file = File(folder, originUrl.fileName)
        if (file.exists() && file.length() > 0) {
            doAsync {
                val options = BitmapFactory.Options()
                // 如果原图大小超过3M，进行压缩
                val bitmap = if (file.length() > 3 * 1024 * 1024) {
                    compressBitmap(file.absolutePath)
                } else {
                    BitmapFactory.decodeFile(file.absolutePath, options)
                }
                runOnUiThread {
                    iv_fragment_preview.setImageBitmap(bitmap)
                    shareElementLoad()
                }
            }
            return true
        } else {
            return false
        }
    }

    /**
     * 显示角标
     * 可能存在多张图片的图片集
     */
    private fun showImgSize(view: TextView, bean: IllustsBean) {
        view.apply {
            if (bean.metaPages.isNotEmpty()) {
                visibility = View.VISIBLE
                text = "${bean.metaPages.size}p"
            } else {
                visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 当图片加载完成之后再加载共享元素动画
     */
    private fun shareElementLoad() {
        if (current) {
            activity?.supportStartPostponedEnterTransition()
        }
    }

    private fun initRelatedList() {
        bean?.let { bean ->
            fl_fragment_preview_related.apply {
                visibility = View.VISIBLE
                val windowSize = Point()
                requireActivity().windowManager.defaultDisplay.getSize(windowSize)
                layoutParams = layoutParams.apply {
                    height = windowSize.y
                }
            }
            childFragmentManager.beginTransaction().let {
                val fragment = PreviewRelateListFragment()
                fragment.arguments = Bundle().apply {
                    putString("illustsId", bean.id)
                }
                it.replace(R.id.fl_fragment_preview_related, fragment)
                it.commitNow()
            }
        }
    }

    /**
     * 从大图预览页面返回时，如果在大图页面加载了原图则显示原图
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            bean?.let {
                loadPreviewFromFile(it)
            }
        }
    }
}