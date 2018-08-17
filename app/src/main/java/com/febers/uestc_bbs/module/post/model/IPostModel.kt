/*
 * Created by Febers at 18-8-17 下午8:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:46.
 */

package com.febers.uestc_bbs.module.post.model

interface IPostModel {
    fun postService(_postId: String, _page: Int, _authorId: String, _order: String)
}