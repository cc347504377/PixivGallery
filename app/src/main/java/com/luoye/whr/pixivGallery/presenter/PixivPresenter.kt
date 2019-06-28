package com.luoye.whr.pixivGallery.presenter

import com.luoye.whr.pixivGallery.MyApplication
import com.luoye.whr.pixivGallery.common.*
import com.luoye.whr.pixivGallery.model.PixivImageModel
import com.luoye.whr.pixivGallery.model.PixivUserModel
import com.luoye.whr.kotlinlibrary.net.PresenterUtil
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

object PixivImagePresenter {

    private val modeList = arrayOf("day", "week", "month", "week_rookie", "week_original", "day_male", "day_female", "day_r18")
    private val sort = arrayOf("date_desc", "popular_desc")
    private val arrayOfSearchType = arrayOf("", " 500users入り", " 1000users入り", " 5000users入り", " 10000users入り")

    fun getRankList(mode: Int, date: String? = null, callback: IllustCallback<CommonIllustBean>) {
        val d = if (date.isNullOrBlank()) {
            SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000))
        } else {
            date
        }
        val filter = modeList[mode]
        val call = PixivImageModel.getRankList(d!!, filter)
        PresenterUtil.getData(call, callback)
    }

    inline fun <reified T> getNext(nextUrl: String, callback: IllustCallback<T>) {
        val call = PixivImageModel.getNext(nextUrl)
        PresenterUtil.getData(call, callback)
    }

    fun getRecommend(callback: IllustCallback<RecommendBean>) {
        val call = PixivImageModel.getRecommendList()
        PresenterUtil.getData(call, callback)
    }

    fun getSearch(key: String, filterType: Int, sortType: Int, callback: IllustCallback<SearchIllustBean>) {
        val call = PixivImageModel.getSearch(key + arrayOfSearchType[filterType], sort[sortType], "partial_match_for_tags",
                Authorization = SpUtil.auth)
        PresenterUtil.getData(call, callback)
    }

    fun getSearchAutoCompleteKeywords(word: String, callback: IllustCallback<SearchAutocompleteBean>) {
        val call = PixivImageModel.getSearchAutoCompleteKeywords(word)
        PresenterUtil.getData(call, callback)
    }

    fun getIllustTrendTags(callback: IllustCallback<String>) {
        val call = PixivImageModel.getIllustTrendTags()
        PresenterUtil.getJson("推荐tag", call, callback)
    }

    fun getFollowIllusts(callback: IllustCallback<CommonIllustBean>) {
        val call = PixivImageModel.getFollowIllusts()
        PresenterUtil.getData(call, callback)
    }

    fun getLikeIllust(callback: IllustCallback<CommonIllustBean>) {
        val call = PixivImageModel.getLikeIllust()
        PresenterUtil.getData(call, callback)
    }

    fun postLikeIllust(id: Long, callback: PublicCallback.StatCallBack) {
        val call = PixivImageModel.postLikeIllust(id)
        PresenterUtil.getCall("postLikeIllust", call, callback)
    }

    fun postUnlikeIllust(id: Long, callback: PublicCallback.StatCallBack) {
        val call = PixivImageModel.postUnlikeIllust(id)
        PresenterUtil.getCall("postUnlikeIllust", call, callback)
    }

    fun getUserFollowing() {
        val call = PixivImageModel.getUserFollowing()
        PresenterUtil.saveJson("follow", MyApplication.context.get()!!, call)
    }

    fun getUserIllust(userId: String, callback: IllustCallback<UserIllustsBean>) {
        val call = PixivImageModel.getUserIllust(userId)
        PresenterUtil.getData(call, callback)
    }

    fun getRelatedIllust(illustsId: String, callback: IllustCallback<CommonIllustBean>) {
        val call = PixivImageModel.getRelatedIllust(illustsId)
        PresenterUtil.getData(call, callback)
    }
}

object PixivUserPresenter {

    fun postAuthToken(username: String, password: String, callback: IllustCallback<PixivAuthBean>) {
        val call = PixivUserModel.postAuthToken(password.trim(), username.trim())
        PresenterUtil.getData(call, callback)
    }

    fun refreshToken(callback: IllustCallback<PixivAuthBean>) {
        val call = PixivUserModel.refreshToken()
        PresenterUtil.getData(call, callback)
    }
}

interface IllustCallback<T> : PublicCallback.DataCallBack<T> {
    override fun onSuccess(t: T) {
        fun removeR18(illusts: MutableList<IllustsBean>) {
            runBlocking {
                val job = GlobalScope.launch {
                    val r18List = illusts.filterNot {
                        val size = it.tags.filter {
                            it.name == "R-18"
                        }.size
                        size == 0
                    }
                    illusts.removeAll(r18List)
                }
                job.join()
            }
        }
        // 非 adultMode 过滤 R18内容
        if (!SpUtil.adultMode) {
            when (t) {
                is CommonIllustBean -> {
                    removeR18(t.illusts)
                }
                is SearchIllustBean -> {
                    removeR18(t.illusts)
                }
            }
        }
    }
}