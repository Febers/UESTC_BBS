/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.model.PListModelImpl

class PListPresenterImpl(var view: PListContract.View) : PListContract.Presenter(view) {

    private val topicMode: PListContract.Model = PListModelImpl(this)

    override fun pListRequest(fid: Int, page: Int, refresh: Boolean) {
        topicMode.pListService(fid, page, refresh)
    }

    override fun pListResult(event: BaseEvent<PostListBean>) {
        view.showPList(event)
    }
}
