package com.luoye.whr.pixivGallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PixivImageApi {

    @GET("v1/illust/ranking?filter=for_android&type=rank&per_page=20")
    fun getList(
        @Header("Authorization") auth: String,
        @Query("date") date: String,
        @Query("mode") mode: String
    ): Call<ResponseBody>

    @GET("/v1/illust/recommended?filter=for_android&include_ranking_label=false")
    fun getRecommend(@Header("Authorization") paramString: String): Call<ResponseBody>

    @GET
    fun getNext(
        @Header("Authorization") paramString1: String,
        @Url paramString2: String
    ): Call<ResponseBody>

    @GET("/v1/trending-tags/illust?filter=for_android")
    fun getIllustTrendTags(@Header("Authorization") paramString: String): Call<ResponseBody>

    @GET("/v1/search/illust?filter=for_android")
    fun getSearchIllust(
        @Query("word") word: String,
        @Query("sort") sort: String,
        @Query("search_target") search_target: String,
        @Query("bookmark_num") bookmark_num: Int?,
        @Query("duration") duration: String?,
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>

    @GET("/v1/illust/detail?filter=for_android")
    fun getIllust(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Call<ResponseBody>

    @GET("/v1/search/user?filter=for_android")
    fun getSearchUser(
        @Header("Authorization") paramString1: String,
        @Query("word") paramString2: String
    ): Call<ResponseBody>

    @GET("/v1/search/autocomplete")
    fun getSearchAutoCompleteKeywords(
        @Header("Authorization") paramString1: String,
        @Query("word") paramString2: String
    ): Call<ResponseBody>

    // 关注的用户的新作品
    // restrict: public private
    @GET("/v2/illust/follow")
    fun getFollowIllusts(
        @Header("Authorization") paramString1: String,
        @Query("restrict") paramString2: String
    ): Call<ResponseBody>

    // 收藏的作品
    // https://app-api.pixiv.net/v1/user/bookmarks/illust?user_id=31655571&restrict=public
    @GET("/v1/user/bookmarks/illust")
    fun getLikeIllust(
        @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: String,
        @Query("restrict") paramString2: String,
        @Query("tag") paramString3: String?
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    fun postLikeIllust(@Header("Authorization") paramString1: String,
                       @Field("illust_id") paramLong: Long,
                       @Field("restrict") paramString2: String,
                       @Field("tags[]") paramList: List<String>?): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    fun postUnlikeIllust(@Header("Authorization") paramString: String,
                         @Field("illust_id") paramLong: Long): Call<ResponseBody>

    // 关注的用户
    @GET("/v1/user/following?filter=for_android")
    fun getUserFollowing(
        @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: String,
        @Query("restrict") paramString2: String
    ): Call<ResponseBody>

    // 用户的作品
    @GET("/v1/user/illusts?filter=for_ios")
    fun getUserIllusts(
        @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: String,
        @Query("type") paramString2: String?
    ): Call<ResponseBody>

    // &illust_id=62443212
    @GET("/v2/illust/related?filter=for_android")
    fun getRelatedIllust(
        @Header("Authorization") paramString1: String,
        @Query("illust_id") paramLong: String
    ): Call<ResponseBody>

    @GET("/v1/illust/comments")
    fun getIllustComments(
        @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long
    ): Call<ResponseBody>

    @GET
    fun getNextComment(
        @Header("Authorization") paramString1: String,
        @Url paramString2: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    fun postIllustComment(
        @Header("Authorization") paramString1: String,
        @Field("illust_id") paramLong: Long,
        @Field("comment") paramString2: String,
        @Field("parent_comment_id") paramInteger: Int?
    ): Call<ResponseBody>
}

interface PixivUserApi {

    @FormUrlEncoded
    @POST("/auth/token")
    fun postAuthToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("device_token") device_token: String,
        @Field("get_secure_url") get_secure_url: Boolean,
        @Field("grant_type") grant_type: String,
        @Field("password") password: String,
        @Field("username") username: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/auth/token")
    fun refreshToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("device_token") device_token: String,
        @Field("get_secure_url") get_secure_url: Boolean,
        @Field("grant_type") grant_type: String,
        @Field("refresh_token") refresh_token: String
    ): Call<ResponseBody>
}