package com.luoye.whr.gallery.presenter

import com.luoye.whr.gallery.MyApplication
import com.luoye.whr.gallery.common.*
import com.luoye.whr.gallery.model.PixivImageModel
import com.luoye.whr.gallery.model.PixivUserModel
import com.luoye.whr.kotlinlibrary.net.PresenterUtil
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import java.text.SimpleDateFormat
import java.util.*

object PixivImagePresenter {

    private val modeList = arrayOf("day", "week", "month", "week_rookie", "week_original", "day_male", "day_female", "day_r18")
    private val sort = arrayOf("date_desc", "popular_desc")
    private val arrayOfSearchType = arrayOf("", " 500users入り", " 1000users入り", " 5000users入り", " 10000users入り")

    fun getRankList(mode: Int, date: String? = null, callback: PublicCallback.DataCallBack<CommonIllustBean>) {
        val d = if (date.isNullOrBlank()) {
            SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000))
        } else {
            date
        }
        val filter = modeList[mode]
        val call = PixivImageModel.getRankList(d!!, filter)
        PresenterUtil.getData(call, callback)
    }

    inline fun <reified T> getNext(nextUrl: String, callback: PublicCallback.DataCallBack<T>) {
        val call = PixivImageModel.getNext(nextUrl)
        PresenterUtil.getData(call, callback)
    }

    fun getRecommend(callback: PublicCallback.DataCallBack<RecommendBean>) {
        val call = PixivImageModel.getRecommendList()
        PresenterUtil.getData(call, callback)
    }

    fun getSearch(key: String, filterType: Int, sortType: Int, callback: PublicCallback.DataCallBack<SearchIllustBean>) {
        val call = PixivImageModel.getSearch(key + arrayOfSearchType[filterType], sort[sortType], "partial_match_for_tags",
                Authorization = SpUtil.auth)
        PresenterUtil.getData(call, callback)
    }

    fun getSearchAutoCompleteKeywords(word: String, callback: PublicCallback.DataCallBack<SearchAutocompleteBean>) {
        val call = PixivImageModel.getSearchAutoCompleteKeywords(word)
        PresenterUtil.getData(call, callback)
    }

    fun getIllustTrendTags(callback: PublicCallback.DataCallBack<String>) {
        val call = PixivImageModel.getIllustTrendTags()
        PresenterUtil.getJson("推荐tag", call, callback)
    }

    fun getFollowIllusts(callback: PublicCallback.DataCallBack<CommonIllustBean>) {
        val call = PixivImageModel.getFollowIllusts()
        PresenterUtil.getData(call, callback)
    }

    fun getLikeIllust(callback: PublicCallback.DataCallBack<CommonIllustBean>) {
        val call = PixivImageModel.getLikeIllust()
        PresenterUtil.getData(call, callback)
    }

    fun getUserFollowing() {
        val call = PixivImageModel.getUserFollowing()
        PresenterUtil.saveJson("follow", MyApplication.context.get()!!, call)
    }

    fun getUserIllust(userId: String, callback: PublicCallback.DataCallBack<UserIllustsBean>) {
        val call = PixivImageModel.getUserIllust(userId)
        PresenterUtil.getData(call, callback)
    }

    fun getRelatedIllust(illustsId: String, callback: PublicCallback.DataCallBack<CommonIllustBean>) {
        val call = PixivImageModel.getRelatedIllust(illustsId)
        PresenterUtil.getData(call, callback)
    }
}

object PixivUserPresenter {

    fun postAuthToken(username: String, password: String, callback: PublicCallback.DataCallBack<PixivAuthBean>) {
        val call = PixivUserModel.postAuthToken(password.trim(), username.trim())
        PresenterUtil.getData(call, callback)
    }

    fun refreshToken(callback: PublicCallback.DataCallBack<PixivAuthBean>) {
        val call = PixivUserModel.refreshToken()
        PresenterUtil.getData(call, callback)
    }
}