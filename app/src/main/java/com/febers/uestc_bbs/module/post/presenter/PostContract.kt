/*
 * Created by Febers at 18-8-17 下午8:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:45.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.base.ITEM_ORDER_POSITIVE
import com.febers.uestc_bbs.entity.PostDetailBean

interface PostContract {

    interface Model {
        fun postService(postId: String, page: Int, authorId: Int, order: Int)
    }

    interface View: BaseView {
        fun showPost(event: BaseEvent<PostDetailBean>)
    }
    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun postRequest(postId: String, page: Int, authorId: Int = 0, order: Int = ITEM_ORDER_POSITIVE)
        abstract fun postResult(event: BaseEvent<PostDetailBean>)
    }
}