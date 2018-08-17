/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import android.util.Log
import android.util.Log.i
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SimplePostBean
import com.febers.uestc_bbs.module.post.model.IPostModel
import com.febers.uestc_bbs.module.post.model.PostModelImpl

class PostPresenterImpl(mView: PostContract.View) : PostContract.Presenter(mView) {

    override fun postRequest(fid: String, page: Int, refresh: Boolean) {
        val postMode: IPostModel = PostModelImpl(this)
        postMode.postService(fid, page, refresh)
    }

    override fun postResult(event: BaseEvent<List<SimplePostBean>?>) {
        mView?.postResult(event)
    }
}
