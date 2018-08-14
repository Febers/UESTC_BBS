/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenter

/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回去，最新发表，热门帖子
 */
class SubPostFragment: BaseFragment(), PostContract.View {

    var position: Int = 0

    companion object {
        fun getInstance(args: Bundle): SubPostFragment {
            val fragment = SubPostFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setContentView(): kotlin.Int {
        return R.layout.fragment_sub_post
    }

    override fun initView() {
        val bundle = arguments
        this.position = bundle?.getInt("position") as Int
    }

    override fun lazyLoad() {
        getPosts()
    }

    override fun getPosts() {
        val p = PostPresenter(this)
        p.getPosts(position)
    }
}