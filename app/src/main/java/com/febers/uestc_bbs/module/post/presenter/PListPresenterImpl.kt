/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.module.post.model.IPListModel
import com.febers.uestc_bbs.module.post.model.TopicModelImpl

class PListPresenterImpl(mView: PListContract.View) : PListContract.Presenter(mView) {

    override fun pListRequest(fid: String, page: Int, refresh: Boolean) {
        val topicMode: IPListModel = TopicModelImpl(this)
        topicMode.topicService(fid, page, refresh)
    }

    override fun pListResult(event: BaseEvent<List<SimplePListBean>?>) {
        mView?.pListResult(event)
    }
}
