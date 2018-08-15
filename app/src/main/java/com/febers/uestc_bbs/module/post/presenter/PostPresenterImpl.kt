/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.presenter

import android.util.Log
import android.util.Log.i

class PostPresenterImpl(mView: PostContract.View) : PostContract.Presenter(mView) {

    override fun postRequest(fid: String, page: Int, refresh: Boolean) {
        i("PPP", "postResult: $fid")
    }

}
