/*
 * Created by Febers at 18-8-15 下午11:36.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:34.
 */

package com.febers.uestc_bbs.module.post.model

interface IPostModel {
    fun postService(_fid: String, _page: String, _refresh: Boolean)
}