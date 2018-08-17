/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SimpleTopicBean
import com.febers.uestc_bbs.module.post.model.ITopicModel
import com.febers.uestc_bbs.module.post.model.TopicModelImpl

class TopicPresenterImpl(mView: TopicContract.View) : TopicContract.Presenter(mView) {

    override fun topicRequest(fid: String, page: Int, refresh: Boolean) {
        val topicMode: ITopicModel = TopicModelImpl(this)
        topicMode.topicService(fid, page, refresh)
    }

    override fun topicResult(event: BaseEvent<List<SimpleTopicBean>?>) {
        mView?.topicResult(event)
    }
}
