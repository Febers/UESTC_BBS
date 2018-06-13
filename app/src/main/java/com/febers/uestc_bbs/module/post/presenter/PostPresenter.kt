/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import android.util.Log

class PostPresenter : PostContract.Presenter {

    constructor(mView: PostContract.View): super(mView)

    override fun getPosts(position: Int) {
        Log.i(TAG, "getPosts: $position")
    }

    companion object {
        private val TAG = "PostPresenter"
    }
}
