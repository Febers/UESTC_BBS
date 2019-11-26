package com.febers.uestc_bbs.module.setting

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.MyApp.Companion.postItemVisibleSetting
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.DEFAULT_POST_ITEM_VISIBLE_VALUE
import com.febers.uestc_bbs.base.SP_POST_ITEM_VISIBLE
import com.febers.uestc_bbs.module.dialog.ChoiceAdapter
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.colorAccent
import com.febers.uestc_bbs.utils.logi
import kotlinx.android.synthetic.main.activity_post_item_setting.*
import kotlinx.android.synthetic.main.item_layout_post.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class PostItemSettingActivity: BaseActivity() {

    private var visibleValue by PreferenceUtils(ctx, SP_POST_ITEM_VISIBLE, DEFAULT_POST_ITEM_VISIBLE_VALUE)
    private var tempItems: MutableList<Int> = ArrayList()

    private lateinit var choiceAdapter: ChoiceAdapter

    override fun setView(): Int = R.layout.activity_post_item_setting
    override fun setToolbar(): Toolbar? = toolbar_common
    override fun setTitle(): String? = "自定义样式"

    override fun initView() { }

    override fun afterCreated() {
        if (!visibleValue.contains(",")) {
            if (visibleValue.isNotEmpty()) {
                tempItems.add(visibleValue.toInt())
            }
        } else {
            tempItems.addAll(visibleValue.split(",").map { it.toInt() }.toTypedArray().toMutableList())
        }
        initVisible()

        val titles = listOf("作者头像", "作者与时间", "帖子摘要", "版块名称", "阅读数量", "评论数量")
        logi { "visibleValue: $visibleValue, setting: $tempItems" }

        choiceAdapter = ChoiceAdapter(context = ctx, titles = titles, messages = emptyList(), checked = tempItems).apply {
            setOnItemChildClickListener(R.id.check_item_choice_dialog) {
                viewHolder, data, position ->
                onChangeVisible(position)
                if (tempItems.contains(position)) {
                    tempItems.remove(position)
                } else {
                    tempItems.add(position)
                }
            }
        }
        recycler_view_post_item_setting.adapter = choiceAdapter
        btn_reset.setTextColor(colorAccent())
        btn_save.setTextColor(colorAccent())
        btn_reset.setOnClickListener {
            resetVisible()
        }
        btn_save.setOnClickListener {
            val sb = StringBuilder()
            tempItems.forEach {
                when (it) {
                    0 -> {
                        sb.append("0,")
                    }
                    1 -> {
                        sb.append("1,")
                    }
                    2 -> {
                        sb.append("2,")
                    }
                    3 -> {
                        sb.append("3,")
                    }
                    4 -> {
                        sb.append("4,")
                    }
                    5 -> {
                        sb.append("5,")
                    }
                }
            }
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(sb.lastIndex)
            }
            visibleValue = sb.toString()
            postItemVisibleSetting.clear()
            postItemVisibleSetting.addAll(tempItems)
            showHint("保存成功")
        }
    }

    private fun initVisible() {
        val default = listOf(0,1,2,3,4,5)
        val different: List<Int> = default.filter { it !in tempItems }
        different.forEach { int ->
            when (int) {
                0 -> {
                    image_view_item_post_avatar.visibility = View.GONE
                }
                1 -> {
                    arrayOf(text_view_item_poster, text_view_item_post_time).forEach { view ->
                        view.visibility = View.GONE
                    }
                }
                2 -> {
                    text_view_item_post_content.visibility = View.GONE
                }
                3 -> {
                    text_view_item_post_block.visibility = View.GONE
                }
                4 -> {
                    arrayOf(text_view_item_post_hits, image_view_item_post_hits).forEach { view ->
                        view.visibility = View.GONE
                    }
                }
                5 -> {
                    arrayOf(text_view_item_post_reply, image_view_reply_list).forEach { view ->
                        view.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun onChangeVisible(position: Int) {
        val views: Array<View>? = when (position) {
            0 -> {
                arrayOf(image_view_item_post_avatar)
            }
            1 -> { arrayOf(text_view_item_poster, text_view_item_post_time)
            }
            2 -> {
                arrayOf(text_view_item_post_content)
            }
            3 -> {
                arrayOf(text_view_item_post_block)
            }
            4 -> {
                arrayOf(text_view_item_post_hits, image_view_item_post_hits)
            }
            5 -> {
                arrayOf(text_view_item_post_reply, image_view_reply_list)
            }
            else -> { null }
        }
        views?.let {
            it.forEach { view ->
                if (view.visibility != View.VISIBLE) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        }
    }

    private fun resetVisible() {
        visibleValue = DEFAULT_POST_ITEM_VISIBLE_VALUE
        postItemVisibleSetting.clear()
        postItemVisibleSetting.addAll(arrayOf(0, 1, 2, 3, 4, 5))
        tempItems.clear()
        tempItems.addAll(postItemVisibleSetting)
        arrayOf(image_view_item_post_avatar, text_view_item_poster, text_view_item_post_time,
                text_view_item_post_content, text_view_item_post_block, text_view_item_post_hits,
                image_view_item_post_hits, text_view_item_post_reply, image_view_reply_list).forEach {
            if (it.visibility != View.VISIBLE) {
                it.visibility = View.VISIBLE
            }
        }
        choiceAdapter.checked = tempItems
        choiceAdapter.notifyDataSetChanged()
    }
}