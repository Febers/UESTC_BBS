package com.febers.uestc_bbs.entity

data class GithubReleaseBean(
    var assets: List<Asset?>?,
    var assets_url: String?,
    var author: Author?,
    var body: String?,
    var created_at: String?,
    var draft: Boolean?,
    var html_url: String?,
    var id: Int?,
    var name: String?,
    var node_id: String?,
    var prerelease: Boolean?,
    var published_at: String?,
    var tag_name: String?,
    var tarball_url: String?,
    var target_commitish: String?,
    var upload_url: String?,
    var url: String?,
    var zipball_url: String?
)

data class Asset(
    var browser_download_url: String?,
    var content_type: String?,
    var created_at: String?,
    var download_count: Int?,
    var id: Int?,
    var label: Any?,
    var name: String?,
    var node_id: String?,
    var size: Int?,
    var state: String?,
    var updated_at: String?,
    var uploader: Uploader?,
    var url: String?
)

data class Uploader(
    var avatar_url: String?,
    var events_url: String?,
    var followers_url: String?,
    var following_url: String?,
    var gists_url: String?,
    var gravatar_id: String?,
    var html_url: String?,
    var id: Int?,
    var login: String?,
    var node_id: String?,
    var organizations_url: String?,
    var received_events_url: String?,
    var repos_url: String?,
    var site_admin: Boolean?,
    var starred_url: String?,
    var subscriptions_url: String?,
    var type: String?,
    var url: String?
)

data class Author(
    var avatar_url: String?,
    var events_url: String?,
    var followers_url: String?,
    var following_url: String?,
    var gists_url: String?,
    var gravatar_id: String?,
    var html_url: String?,
    var id: Int?,
    var login: String?,
    var node_id: String?,
    var organizations_url: String?,
    var received_events_url: String?,
    var repos_url: String?,
    var site_admin: Boolean?,
    var starred_url: String?,
    var subscriptions_url: String?,
    var type: String?,
    var url: String?
)