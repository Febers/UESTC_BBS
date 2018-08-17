/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.SimplePostBean

interface PostContract {
    interface View : BaseView {
        fun postResult(event: BaseEvent<List<SimplePostBean>?>)
    }

    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun postRequest(fid: String, page: Int, refresh: Boolean)
        abstract fun postResult(event: BaseEvent<List<SimplePostBean>?>)
    }
}