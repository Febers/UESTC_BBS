/*
 * Created by Febers at 18-8-18 上午1:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 上午1:32.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.post.model.PostModelImpl

class PostPresenterImpl(var view: PostContract.View): PostContract.Presenter(view) {

    override fun postRequest(postId: Int, page: Int, authorId: Int, order: Int) {
        val postModel: PostContract.Model = PostModelImpl(this)
        postModel.postDetailService(postId, page, authorId, order)
    }

    override fun postResult(event: BaseEvent<PostDetailBean>) {
        view.showPost(event)
    }
}