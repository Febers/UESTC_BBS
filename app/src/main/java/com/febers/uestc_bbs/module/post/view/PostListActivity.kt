/*
 * Created by Febers at 18-6-9 下午9:08.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 下午9:08.
 */

package com.febers.uestc_bbs.module.post.view

import android.util.Log.d
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_post_list.*

class PostListActivity: BaseActivity() {

    private var group = 0
    private var position = 0

    override fun setView(): Int {
        group = intent.getIntExtra("group", 0)
        position = intent.getIntExtra("position", 0)
        return R.layout.activity_post_list
    }

    override fun initView() {
        setSupportActionBar(toolbar_post_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("板块名称")
        d("post_list", "group:$group position:$position")
    }
}