package com.febers.uestc_bbs.module.user.view

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.DetailItemAdapter
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseSwipeActivty
import com.febers.uestc_bbs.entity.DetailItemBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.view.utils.BlurTransformation
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import kotlinx.android.synthetic.main.fragment_user_detail.*

class UserDetailActivity : BaseSwipeActivty() {

    private lateinit var user: UserBean

    override fun noStatusBar(): Boolean = true

    override fun setView(): Int {
        return R.layout.activity_user_detail
    }

    override fun initView() {
        user = BaseApplication.getUser()
        recyclerview_detail_fragment.layoutManager = LinearLayoutManager(this)
        recyclerview_detail_fragment.adapter = DetailItemAdapter(this, initItem(), false)
        recyclerview_detail_fragment.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        Glide.with(this).load(user.avatar).transform(BlurTransformation(this)).into(image_view_detail_blur_avatar)
        Glide.with(this).load(user.avatar).transform(GlideCircleTransform(this)).into(image_view_detail_avatar)
    }

    private fun initItem(): List<DetailItemBean> {
        val item1 = DetailItemBean("名字", user.name)
        val item2 = DetailItemBean("性别", user.gender)
        val item3 = DetailItemBean("等级", user.title)
        val item4 = DetailItemBean("积分", user.credits)
        val item5 = DetailItemBean("水滴", user.extcredits2)
        return arrayListOf(item1, item2, item3, item4, item5)
    }
}
