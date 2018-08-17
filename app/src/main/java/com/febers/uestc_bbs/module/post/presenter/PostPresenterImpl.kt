/*
 * Created by Febers at 18-8-18 上午1:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 上午1:32.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.model.IPostModel
import com.febers.uestc_bbs.module.post.model.PostModelImpl

class PostPresenterImpl(mView: PostContract.View): PostContract.Presenter(mView) {
    override fun postRequest(postId: String, page: Int, authorId: String, order: String) {
        val postModel: IPostModel = PostModelImpl(this)
        postModel.postService(postId, page, authorId, order)
    }

    override fun postResult(event: BaseEvent<PostResultBean>) {
        mView?.postResult(event)
    }
}