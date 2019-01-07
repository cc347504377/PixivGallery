package com.luoye.whr.gallery.common

import com.google.gson.annotations.SerializedName

data class CommonIllustBean(
        var illusts: MutableList<IllustsBean>,
        var next_url: String = "")

data class RecommendBean(
        var isContest_exists: Boolean = false,
        var illusts: MutableList<IllustsBean>,
        var next_url: String? = null
)

data class SearchIllustBean(
        var illusts: MutableList<IllustsBean>,
        var next_url: String? = null,
        var search_span_limit: Int = 0
)

data class FollowUserBean(
        @SerializedName("user_previews") val userPreviews: List<UserPreview>,
        @SerializedName("next_url") val nextUrl: String?
) {

    data class UserPreview(
            @SerializedName("user") val user: User?,
            @SerializedName("illusts") val illusts: MutableList<IllustsBean?>?,
            @SerializedName("novels") val novels: List<Any>?,
            @SerializedName("is_muted") val isMuted: Boolean?
    ) {

        data class User(
                @SerializedName("id") val id: Int?,
                @SerializedName("name") val name: String?,
                @SerializedName("account") val account: String?,
                @SerializedName("profile_image_urls") val profileImageUrls: ProfileImageUrls?,
                @SerializedName("is_followed") val isFollowed: Boolean?
        ) {

            data class ProfileImageUrls(
                    @SerializedName("medium") val medium: String?
            )
        }
    }
}

data class UserIllustsBean(
        @SerializedName("illusts") val illusts: MutableList<IllustsBean>,
        @SerializedName("next_url") val nextUrl: String?
)

data class IllustsBean(
        @SerializedName("caption") val caption: String = "",
        @SerializedName("create_date") val createDate: String = "",
        @SerializedName("height") val height: Int = 0,
        @SerializedName("id") val id: String = "",
        @SerializedName("image_urls") val imageUrls: ImageUrls = ImageUrls(),
        @SerializedName("isIs_bookmarked") val isIsBookmarked: Boolean = false,
        @SerializedName("isIs_muted") val isIsMuted: Boolean = false,
        @SerializedName("isVisible") val isVisible: Boolean = false,
        @SerializedName("meta_pages") val metaPages: List<MetaPage> = listOf(),
        @SerializedName("meta_single_page") val metaSinglePage: MetaSinglePage = MetaSinglePage(),
        @SerializedName("page_count") val pageCount: Int = 0,
        @SerializedName("restrict") val restrict: Int = 0,
        @SerializedName("sanity_level") val sanityLevel: Int = 0,
        @SerializedName("tags") val tags: List<Tag> = listOf(),
        @SerializedName("title") val title: String = "",
        @SerializedName("total_bookmarks") val totalBookmarks: Int = 0,
        @SerializedName("total_view") val totalView: Int = 0,
        @SerializedName("type") val type: String = "",
        @SerializedName("user") val user: User = User(),
        @SerializedName("width") val width: Int = 0
) {
    data class MetaPage(
            @SerializedName("image_urls") val imageUrls: ImageUrls = ImageUrls()
    ) {
        data class ImageUrls(
                @SerializedName("square_medium") val squareMedium: String = "",
                @SerializedName("medium") val medium: String = "",
                @SerializedName("large") val large: String = "",
                @SerializedName("original") val original: String = ""
        )
    }

    data class MetaSinglePage(
            @SerializedName("original_image_url") val originalImageUrl: String = ""
    )

    data class User(
            @SerializedName("account") val account: String = "",
            @SerializedName("id") val id: Int = 0,
            @SerializedName("isIs_followed") val isIsFollowed: Boolean = false,
            @SerializedName("name") val name: String = "",
            @SerializedName("profile_image_urls") val profileImageUrls: ProfileImageUrls = ProfileImageUrls()
    ) {
        data class ProfileImageUrls(
                @SerializedName("medium") val medium: String = ""
        )
    }

    data class ImageUrls(
            @SerializedName("large") val large: String = "",
            @SerializedName("medium") val medium: String = "",
            @SerializedName("square_medium") val squareMedium: String = ""
    )

    data class Tag(
            @SerializedName("name") val name: String = ""
    )
}

data class PixivAuthBean(
    @SerializedName("response")
    val response: Response = Response()
) {
    data class Response(
        @SerializedName("access_token")
        val accessToken: String = "",
        @SerializedName("device_token")
        val deviceToken: String = "",
        @SerializedName("expires_in")
        val expiresIn: Int = 0,
        @SerializedName("refresh_token")
        val refreshToken: String = "",
        @SerializedName("scope")
        val scope: String = "",
        @SerializedName("token_type")
        val tokenType: String = "",
        @SerializedName("user")
        val user: User = User()
    ) {
        data class User(
            @SerializedName("account")
            val account: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("is_mail_authorized")
            val isMailAuthorized: Boolean = false,
            @SerializedName("is_premium")
            val isPremium: Boolean = false,
            @SerializedName("mail_address")
            val mailAddress: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("profile_image_urls")
            val profileImageUrls: ProfileImageUrls = ProfileImageUrls(),
            @SerializedName("x_restrict")
            val xRestrict: Int = 0
        ) {
            data class ProfileImageUrls(
                @SerializedName("px_16x16")
                val px16x16: String = "",
                @SerializedName("px_170x170")
                val px170x170: String = "",
                @SerializedName("px_50x50")
                val px50x50: String = ""
            )
        }
    }
}

data class SearchAutocompleteBean(val search_auto_complete_keywords: List<String>?)

data class RecommendTagBean(
        @SerializedName("trend_tags") val trendTags: List<TrendTag> = listOf()
) {

    data class TrendTag(
            @SerializedName("tag") val tag: String = "",
            @SerializedName("illust") val illust: Illust = Illust()
    ) {

        data class Illust(
                @SerializedName("id") val id: Int = 0,
                @SerializedName("title") val title: String = "",
                @SerializedName("type") val type: String = "",
                @SerializedName("image_urls") val imageUrls: ImageUrls = ImageUrls(),
                @SerializedName("caption") val caption: String = "",
                @SerializedName("restrict") val restrict: Int = 0,
                @SerializedName("user") val user: User = User(),
                @SerializedName("tags") val tags: List<Tag> = listOf(),
                @SerializedName("tools") val tools: List<String> = listOf(),
                @SerializedName("create_date") val createDate: String = "",
                @SerializedName("page_count") val pageCount: Int = 0,
                @SerializedName("width") val width: Int = 0,
                @SerializedName("height") val height: Int = 0,
                @SerializedName("sanity_level") val sanityLevel: Int = 0,
                @SerializedName("series") val series: Any = Any(),
                @SerializedName("meta_single_page") val metaSinglePage: MetaSinglePage = MetaSinglePage(),
                @SerializedName("meta_pages") val metaPages: List<Any> = listOf(),
                @SerializedName("total_view") val totalView: Int = 0,
                @SerializedName("total_bookmarks") val totalBookmarks: Int = 0,
                @SerializedName("is_bookmarked") val isBookmarked: Boolean = false,
                @SerializedName("visible") val visible: Boolean = false,
                @SerializedName("is_muted") val isMuted: Boolean = false
        ) {

            data class User(
                    @SerializedName("id") val id: Int = 0,
                    @SerializedName("name") val name: String = "",
                    @SerializedName("account") val account: String = "",
                    @SerializedName("profile_image_urls") val profileImageUrls: ProfileImageUrls = ProfileImageUrls(),
                    @SerializedName("is_followed") val isFollowed: Boolean = false
            ) {

                data class ProfileImageUrls(
                        @SerializedName("medium") val medium: String = ""
                )
            }


            data class ImageUrls(
                    @SerializedName("square_medium") val squareMedium: String = "",
                    @SerializedName("medium") val medium: String = "",
                    @SerializedName("large") val large: String = ""
            )


            data class MetaSinglePage(
                    @SerializedName("original_image_url") val originalImageUrl: String = ""
            )


            data class Tag(
                    @SerializedName("name") val name: String = ""
            )
        }
    }
}
