/*
 * Created by Febers at 18-8-15 下午11:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:38.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.module.post.presenter.PostContract

class PostModelImpl(val postPresenter: PostContract.Presenter) : IPostModel {



    override fun postService(_fid: String, _page: String, _refresh: Boolean) {

    }
}