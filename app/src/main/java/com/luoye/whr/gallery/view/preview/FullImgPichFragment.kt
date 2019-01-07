package com.luoye.whr.gallery.view.preview

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luoye.whr.gallery.MyApplication
import com.luoye.whr.gallery.R
import com.luoye.whr.gallery.common.folder
import com.luoye.whr.gallery.common.loadPixvRawImg
import com.luoye.whr.gallery.model.ImgModel
import com.luoye.whr.kotlinlibrary.download.RequestCallback
import com.luoye.whr.kotlinlibrary.util.*
import kotlinx.android.synthetic.main.fragment_full_img_pinch.*
import org.jetbrains.anko.doAsync
import java.io.File

class FullImgPichFragment : Fragment(), RequestCallback {

    override fun onDownloadStart() {
        if (!canLoad) {
            return
        }
        runOnUiThread {
            showProgress()
        }
    }

    override fun onSuccess(body: String) {
        if (!canLoad) {
            return
        }
        activity?.setResult(Activity.RESULT_OK)
        originPath = body
        val options = BitmapFactory.Options()
        //如果原图大小超过3M，进行压缩
        val bitmap = if (File(body).length() > 3 * 1024 * 1024) {
            compressBitmap(body)
        } else {
            BitmapFactory.decodeFile(body, options)
        }
        runOnUiThread {
            fab_pinch_download.show()
            pi_pinch_view.setImageBitmap(bitmap)
            dismissProgress()
        }
    }

    override fun onConnectFailed(msg: String) {
        if (!canLoad) {
            return
        }
        runOnUiThread {
            dismissProgress()
            toast(msg)
        }
    }

    override fun onQuestFailed(code: Int, msg: String) {
        if (!canLoad) {
            return
        }
        runOnUiThread {
            dismissProgress()
            toast(msg)
        }
    }

    override fun onProgress(progress: Int) {
        if (!canLoad) {
            return
        }
        runOnUiThread {
            pb_pinch_progress?.progress = progress
        }
    }

    private var canLoad = true
    private var originPath: String? = null
    private var url = ""
    private var originUrl = ""
    private var pId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_full_img_pinch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        canLoad = true
        initView()
        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        canLoad = false
    }

    private fun initListener() {
        pi_pinch_view.setOnClickListener {
            loadOriginImg()
        }
        fab_pinch_download.setOnClickListener {
            downloadImg()
        }
    }

    private fun initView() {
        val file = File(folder, originUrl.fileName)
        if (file.exists() && file.length() > 0) {
            loadOriginImg()
            fab_pinch_download.show()
        } else {
            requireActivity().loadPixvRawImg(url, pId, pi_pinch_view)
            fab_pinch_download.hide()
        }
    }

    private fun loadOriginImg() {
        ImgModel.downloadOriginImg(originUrl, pId, this)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            url = it.getString("url", "")
            originUrl = it.getString("originUrl", "")
            pId = it.getString("pId", "")
        }
    }

    private fun downloadImg() {
        doAsync {
            val saveFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath +
                    "/${requireContext().getAppName()}/${originPath!!.fileName}"
            try {
                file2File(originPath!!, saveFile)
                addImageToSystem(saveFile, MyApplication.context.get()?.contentResolver)
                runOnUiThread {
                    toast("下载完成")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    toast(e.message)
                }
            }
        }
    }

    private fun dismissProgress() {
        pi_pinch_view.foreground = null
        pb_pinch_progress.visibility = View.INVISIBLE
        pi_pinch_view.isEnabled = true
    }

    private fun showProgress() {
        pi_pinch_view.foreground = ColorDrawable(resources.getColor(R.color.colorMask, null))
        pb_pinch_progress.visibility = View.VISIBLE
        pi_pinch_view.isEnabled = false
    }

}