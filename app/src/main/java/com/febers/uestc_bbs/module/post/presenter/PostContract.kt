/*
 * Created by Febers at 18-8-17 下午8:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:45.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.PostResultBean

interface PostContract {

    interface Model {
        fun postService(_postId: String, _page: Int, _authorId: String, _order: String)
    }

    interface View: BaseView {
        fun showPost(event: BaseEvent<PostResultBean>)
    }
    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun postRequest(postId: String, page: Int, authorId: String = "", order: String = "")
        abstract fun postResult(event: BaseEvent<PostResultBean>)
    }
}