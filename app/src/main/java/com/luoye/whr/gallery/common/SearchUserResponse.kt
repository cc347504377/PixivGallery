package com.luoye.whr.gallery.common

class SearchUserResponse {
    var next_url: String? = null
    var user_previews: List<UserPreviewsBean>? = null

    class UserPreviewsBean {
        var illusts: List<IllustsBean>? = null
        var isIs_muted: Boolean = false
        var novels: List<*>? = null
        var user: UserBean? = null

        class UserBean {
            var account: String? = null
            var id: Int = 0
            var isIs_followed: Boolean = false
            var name: String? = null
            var profile_image_urls: ProfileImageUrlsBean? = null

            class ProfileImageUrlsBean {
                var medium: String? = null
            }
        }
    }
}