/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.SimpleTopicBean

interface TopicContract {
    interface View : BaseView {
        fun topicResult(event: BaseEvent<List<SimpleTopicBean>?>)
    }

    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun topicRequest(fid: String, page: Int, refresh: Boolean)
        abstract fun topicResult(event: BaseEvent<List<SimpleTopicBean>?>)
    }
}