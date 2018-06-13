/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView

interface PostContract {
    interface View : BaseView {
        fun getPosts()
    }

    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun getPosts(position: Int)
    }
}