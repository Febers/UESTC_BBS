/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.post

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sub_posts.*
import org.jetbrains.anko.toast

/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回去，最新发表，热门帖子
 */
class SubPostsFragment: BaseFragment(), PostContract.View {

    var position: Int = 0

    companion object {
        fun getInstance(args: Bundle): SubPostsFragment {
            val fragment = SubPostsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setContentView(): kotlin.Int {
        return R.layout.fragment_sub_posts
    }

    override fun initView() {
        val bundle = arguments
        this.position = bundle?.getInt("position") as Int
        image_view_sub_posts.setImageResource(R.drawable.ic_android_blue_24dp)
    }

    override fun lazyLoad() {
        getPosts()
    }

    override fun onCompleted(any: Any) {

    }

    override fun onError(error: String) {
        context?.toast(error)
    }

    override fun getPosts() {
        val p = PostPresenter(this)
        p.getPosts(position)
    }

}