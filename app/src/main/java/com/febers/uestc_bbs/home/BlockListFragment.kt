/*
 * Created by Febers at 18-6-9 下午5:52.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 下午5:52.
 */

package com.febers.uestc_bbs.home

import android.content.Intent
import android.support.v4.util.ArrayMap
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.module.post.view.PostListActivity
import kotlinx.android.synthetic.main.fragment_forum_list.*
import java.util.ArrayList

class BlockListFragment: BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_forum_list
    }

    override fun initView() {
        val from = arrayOf("image", "title")
        val to = intArrayOf(R.id.image_view_forum_list_item, R.id.text_view_forum_list_item)
        val compusAdapter = SimpleAdapter(context, compusGridList(), R.layout.item_forum_list_grid_view, from, to)
        val techAdapter = SimpleAdapter(context, techGridList(), R.layout.item_forum_list_grid_view, from, to)
        val playAdapter = SimpleAdapter(context, playGridList(), R.layout.item_forum_list_grid_view, from, to)
        val marketAdapter = SimpleAdapter(context, marketGridList(), R.layout.item_forum_list_grid_view, from, to)
        val manageAdapter = SimpleAdapter(context, manageGridList(), R.layout.item_forum_list_grid_view, from, to)
        grid_view_compus.adapter = compusAdapter
        grid_view_tech.adapter = techAdapter
        grid_view_play.adapter = playAdapter
        grid_view_market.adapter = marketAdapter
        grid_view_manager.adapter = manageAdapter
        grid_view_compus.onItemClickListener = AdapterView.OnItemClickListener {
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

    override fun lazyLoad() {

    }

    private fun compusGridList(): List<Map<String, Any>> {
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

    private fun onClickGridViewItem(group: Int, position: Int) {
        val intent = Intent(context, PostListActivity::class.java)
        intent.putExtra("group", group)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}