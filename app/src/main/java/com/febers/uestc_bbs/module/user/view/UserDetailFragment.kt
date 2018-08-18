/*
 * Created by Febers at 18-8-16 下午11:09.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 下午11:09.
 */

package com.febers.uestc_bbs.module.user.view

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.DetailItemAdapter
import com.febers.uestc_bbs.base.ARG_PARAM1
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BasePopFragment
import com.febers.uestc_bbs.entity.DetailItemBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.view.GlideCircleTransform
import com.febers.uestc_bbs.view.custom.BlurTransformation
import kotlinx.android.synthetic.main.fragment_user_detail.*

/**
 * 用户个人信息
 */
class UserDetailFragment: BasePopFragment() {

    private lateinit var user: UserBean

    override fun setToolbar(): Toolbar? {
        return toolbar_user_detail
    }

    override fun setContentView(): Int {
        //status透明，实现图片全透明，在退出时恢复,但是回造成home界面的bottomnavigationbar下沉
        //activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        return R.layout.fragment_user_detail
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        recyclerview_detail_fragment.layoutManager = LinearLayoutManager(context)
        recyclerview_detail_fragment.adapter = DetailItemAdapter(context!!, initItem(), false)
        recyclerview_detail_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        Glide.with(this).load(user.avatar).transform(GlideCircleTransform(context)).into(image_view_detail_avatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun initItem(): List<DetailItemBean> {
        val item1 = DetailItemBean("名字", user.name)
        val item2 = DetailItemBean("性别", user.gender)
        val item3 = DetailItemBean("等级", user.title)
        val item4 = DetailItemBean("积分", user.credits)
        val item5 = DetailItemBean("水滴", user.extcredits2)
        return arrayListOf(item1, item2, item3, item4, item5)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                UserDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return attachToSwipeBack(view!!)
    }
}