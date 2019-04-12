package com.luoye.whr.pixivGallery.model

import com.luoye.whr.pixivGallery.api.PixivImageApi
import com.luoye.whr.pixivGallery.api.PixivUserApi
import com.luoye.whr.pixivGallery.common.BASE_URL
import com.luoye.whr.pixivGallery.common.PIXIV_SECURE
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.kotlinlibrary.util.createRetrofit

private const val HEADER_NAME = "User-Agent"
private const val HEADER_VALUE = "PixivAndroidApp/5.0.119 (Android 6.0.1; D6653)"
private const val HEADER_NAME2 = "Accept-Language"
private const val HEADER_VALUE2 = "zh_CN"

object PixivImageModel {

    private val retrofit = createRetrofit(BASE_URL) { it ->
        //        it.dns(HttpDns())
        it.addInterceptor {
            val request = it.request().newBuilder()
                    .addHeader(HEADER_NAME, HEADER_VALUE)
                    .addHeader(HEADER_NAME2, HEADER_VALUE2)
                    .build()
            return@addInterceptor it.proceed(request)
        }
    }
    private val api = retrofit.create(PixivImageApi::class.java)

    /**
     * 排行列表
     */
    fun getRankList(date: String, filter: String) = api.getList(
            getAuth(), date, filter)

    /**
     * 获得下页数据
     */
    fun getNext(nextUrl: String) = api.getNext(getAuth(), nextUrl)

    /**
     * 推荐列表
     */
    fun getRecommendList() = api.getRecommend(getAuth())

    /**
     * 搜索
     */
    fun getSearch(
        word: String,
        sort: String,
        search_target: String,
        bookmark_num: Int? = null,
        duration: String? = null,
        Authorization: String
    ) = api.getSearchIllust(word, sort,
            search_target, bookmark_num, duration, Authorization)

    /**
     * 补完搜索关键字
     */
    fun getSearchAutoCompleteKeywords(word: String) = api.getSearchAutoCompleteKeywords(getAuth(), word)

    /**
     * 推荐tag
     */
    fun getIllustTrendTags() = api.getIllustTrendTags(getAuth())

    /**
     * 关注作品
     */
    fun getFollowIllusts() = api.getFollowIllusts(getAuth(), "public")

    /**
     * 收藏作品
     */
    fun getLikeIllust() = api.getLikeIllust(getAuth(), SpUtil.userId, "public", null)

    fun getUserFollowing() = api.getUserFollowing(getAuth(), SpUtil.userId, "public")

    fun getUserIllust(userId: String) = api.getUserIllusts(getAuth(), userId, null)

    fun getRelatedIllust(illustsId: String) = api.getRelatedIllust(getAuth(), illustsId)

    private fun getAuth() = SpUtil.auth
}

object PixivUserModel {
    private const val client_id = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
    private const val client_secret = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
    private const val device_token = "pixiv"
    private const val grant_type_fresh = "refresh_token"
    private const val get_secure_url = true
    private const val grant_type = "password"

    private val retrofit = createRetrofit(PIXIV_SECURE) { it ->
        //        it.dns(HttpDns())
        it.addInterceptor {
            val request = it.request().newBuilder()
                    .addHeader(HEADER_NAME, HEADER_VALUE)
                    .addHeader(HEADER_NAME2, HEADER_VALUE2)
                    .build()
            return@addInterceptor it.proceed(request)
        }
    }
    private val api = retrofit.create(PixivUserApi::class.java)

    fun postAuthToken(password: String, username: String) = api.postAuthToken(
            client_id, client_secret, device_token, get_secure_url, grant_type, password, username)

    fun refreshToken() = api.refreshToken(
            client_id, client_secret, SpUtil.deviceToken, get_secure_url, grant_type_fresh, SpUtil.refreshToken)
}