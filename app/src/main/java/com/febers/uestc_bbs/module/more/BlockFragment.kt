/*
 * Created by Febers at 18-8-17 下午4:11.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 上午1:59.
 */

package com.febers.uestc_bbs.module.more

import android.os.Bundle
import androidx.collection.ArrayMap
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.login.model.LoginContext
import com.febers.uestc_bbs.module.post.view.PListFragment
import com.febers.uestc_bbs.module.post.view.edit.PostEditFragment
import com.febers.uestc_bbs.utils.BlockUtils
import kotlinx.android.synthetic.main.fragment_block_list.*
import kotlinx.android.synthetic.main.layout_block_list.*
import java.util.ArrayList

class BlockFragment: BaseFragment() {

    private lateinit var mParentFragment: BaseFragment
    private var newPost: Boolean = false

    override fun setContentView(): Int {
        arguments?.let {
            newPost = it.getBoolean(NEW_POST)
        }
        return R.layout.fragment_block_list
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initMyView()
    }

    private fun initMyView() {
        if (newPost) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar_block_list)
            (activity as AppCompatActivity).supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = "选择板块"
            }
            toolbar_block_list.setNavigationOnClickListener { activity?.finish() }
        }
        text_view_title_campus.visibility = View.VISIBLE
        text_view_title_tech.visibility = View.VISIBLE
        text_view_title_play.visibility = View.VISIBLE
        text_view_title_market.visibility = View.VISIBLE
        text_view_title_manager.visibility = View.VISIBLE
        val from = arrayOf("image", "title")
        val to = intArrayOf(R.id.image_view_forum_list_item, R.id.text_view_forum_list_item)
        val campusAdapter = SimpleAdapter(context, campusGridList(), R.layout.item_forum_list_grid_view, from, to)
        val techAdapter = SimpleAdapter(context, techGridList(), R.layout.item_forum_list_grid_view, from, to)
        val playAdapter = SimpleAdapter(context, playGridList(), R.layout.item_forum_list_grid_view, from, to)
        val marketAdapter = SimpleAdapter(context, marketGridList(), R.layout.item_forum_list_grid_view, from, to)
        val manageAdapter = SimpleAdapter(context, manageGridList(), R.layout.item_forum_list_grid_view, from, to)
        grid_view_campus.adapter = campusAdapter
        grid_view_tech.adapter = techAdapter
        grid_view_play.adapter = playAdapter
        grid_view_market.adapter = marketAdapter
        grid_view_manager.adapter = manageAdapter
        grid_view_campus.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ -> onClickGridViewItem(0, position) }
        grid_view_tech.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ -> onClickGridViewItem(1, position) }
        grid_view_play.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ -> onClickGridViewItem(2, position) }
        grid_view_market.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ -> onClickGridViewItem(3, position) }
        grid_view_manager.onItemClickListener = AdapterView.OnItemClickListener {
            _parent, _view, position, _id -> onClickGridViewItem(4, position) }
    }


    private fun campusGridList(): List<Map<String, Any>> {
        val gridList = ArrayList<Map<String, Any>>()
        val titles = arrayOf(
                "就业创业", "学术交流", "出国留学",
                "情感专区", "成电锐评", "校园热点",
                "交通出行", "成电轨迹", "考试专区",
                "同城同乡", "毕业感言", "新生专区")
        val images = intArrayOf(
                R.drawable.ic_forum_gray_hire, R.drawable.ic_forum_gray_xueshu, R.drawable.ic_forum_gray_airplane,
                R.drawable.ic_forum_gray_qinggan, R.drawable.ic_forum_gray_ruiping, R.drawable.ic_forum_gray_compus_hot,
                R.drawable.ic_forum_gray_translation, R.drawable.ic_forum_gray_guiji, R.drawable.ic_forum_gray_exam,
                R.drawable.ic_forum_gray_laoxiang, R.drawable.ic_forum_gray_ganyan, R.drawable.ic_forum_gray_new_student)
        for (i in titles.indices) {
            val map = ArrayMap<String, Any>()
            map["image"] = images[i]
            map["title"] = titles[i]
            gridList.add(map)
        }
        return gridList
    }

    private fun techGridList(): List<Map<String, Any>> {
        val gridList = ArrayList<Map<String, Any>>()
        val titles = arrayOf(
                "自然科学", "前端之美", "科技资讯",
                "数字前端", "电脑FAQ", "硬件数码",
                "Unix/Linux", "程序员", "电子设计")
        val images = intArrayOf(
                R.drawable.ic_forum_gray_science, R.drawable.ic_forum_gray_qianduan, R.drawable.ic_forum_gray_tech,
                R.drawable.ic_forum_gray_digit_qd, R.drawable.ic_forum_gray_computer, R.drawable.ic_forum_gray_hardware,
                R.drawable.ic_forum_gray_linux, R.drawable.ic_forum_gray_coder, R.drawable.ic_forum_gray_electric)
        for (i in titles.indices) {
            val map = ArrayMap<String, Any>()
            map["image"] = images[i]
            map["title"] = titles[i]
            gridList.add(map)
        }
        return gridList
    }

    private fun playGridList(): List<Map<String, Any>> {
        val gridList = ArrayList<Map<String, Any>>()
        val titles = arrayOf(
                "水手之家", "吃喝玩乐", "成电骑迹",
                "视觉艺术", "军事国防", "情系舞缘",
                "音乐空间", "文人墨客", "体坛风云",
                "影视天地", "动漫时代", "跑步公园")
        val images = intArrayOf(
                R.drawable.ic_forum_gray_water, R.drawable.ic_forum_gray_play, R.drawable.ic_forum_gray_cycle,
                R.drawable.ic_forum_gray_camera, R.drawable.ic_forum_gray_army, R.drawable.ic_forum_gray_dance,
                R.drawable.ic_forum_gray_music, R.drawable.ic_forum_gray_wenren, R.drawable.ic_forum_gray_tiyu,
                R.drawable.ic_forum_gray_movie, R.drawable.ic_forum_gray_animal, R.drawable.ic_forum_gray_run)
        for (i in titles.indices) {
            val map = ArrayMap<String, Any>()
            map["image"] = images[i]
            map["title"] = titles[i]
            gridList.add(map)
        }
        return gridList
    }

    private fun marketGridList(): List<Map<String, Any>> {
        val gridList = ArrayList<Map<String, Any>>()
        val titles = arrayOf(
                "二手专区", "房屋租赁", "店铺专区")
        val images = intArrayOf(
                R.drawable.ic_forum_gray_ershou, R.drawable.ic_forum_gray_zufang, R.drawable.ic_forum_gray_shop)
        for (i in titles.indices) {
            val map = ArrayMap<String, Any>()
            map["image"] = images[i]
            map["title"] = titles[i]
            gridList.add(map)
        }
        return gridList
    }

    private fun manageGridList(): List<Map<String, Any>> {
        val gridList = ArrayList<Map<String, Any>>()
        val titles = arrayOf(
                "站务公告", "站务综合")
        val images = intArrayOf(
                R.drawable.ic_forum_gray_gonggao, R.drawable.ic_forum_gray_zonghe)
        for (i in titles.indices) {
            val map = ArrayMap<String, Any>()
            map["image"] = images[i]
            map["title"] = titles[i]
            gridList.add(map)
        }
        return gridList
    }

    /**
     * 根据点击的结果跳转页面
     * 如果用户的行为是要发表帖子，那么newPost为true，将fid传过去即可
     * 如果用户的行为是要查看板块的帖子，则将fid和相应的标题传过去
     * 因为gridView的List包含Map，为了获取其中的标题，采用逆向获取字符串的方式
     *  **List()[position].values.last().toString()
     * @param group 所选择的分组
     * @param position 选项在分组中的位置
     */
    private fun onClickGridViewItem(group: Int, position: Int) {
        if (newPost) {
            startWithPop(PostEditFragment
                    .newInstance(fid = BlockUtils.getBlockIdByPosition(group, position)))

        } else {
            if (!LoginContext.userState(context!!)) return
            //mParentFragment = parentFragment as BaseFragment
            start(PListFragment.newInstance(fid = BlockUtils.getBlockIdByPosition(group, position),
                    title = when(group) {
                        0 -> campusGridList()[position].values.last().toString()
                        1 -> techGridList()[position].values.last().toString()
                        2 -> playGridList()[position].values.last().toString()
                        3 -> marketGridList()[position].values.last().toString()
                        4 -> manageGridList()[position].values.last().toString()
                        else -> ""
                    }, showBottomBarOnDestroy = true))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(newPost: Boolean) =
                BlockFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(NEW_POST, newPost)
                    }
                }
    }
}