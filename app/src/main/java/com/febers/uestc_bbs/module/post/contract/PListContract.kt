/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.PostListBean

interface PListContract {

    interface Model {
        fun pListService(fid: Int, page: Int, refresh: Boolean)
    }

    interface View : BaseView {
        fun showPList(event: BaseEvent<PostListBean>)
    }

    abstract class Presenter(view: BaseView) : BasePresenter<BaseView>(view) {
        abstract fun pListRequest(fid: Int, page: Int, refresh: Boolean)
        abstract fun pListResult(event: BaseEvent<PostListBean>)
    }
}