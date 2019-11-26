package com.febers.uestc_bbs.module.more

import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.io.UserManager
import com.febers.uestc_bbs.module.dialog.Dialog
import com.febers.uestc_bbs.utils.logi
import kotlinx.android.synthetic.main.activity_debug.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class DebugActivity: BaseActivity() {

    override fun setView(): Int = R.layout.activity_debug

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun setTitle(): String? = "Debug"

    override fun initView() {
        val content = intent.getStringExtra("debug")
        tv_debug_result.text = content ?: "Debug模式"
    }

    override fun afterCreated() {
        layout_debug_container.addView(Button(ctx).apply {
            text = "查看用户"
            setOnClickListener {
                tv_debug_result.text = UserManager.getAllUser().toString()
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "加载中弹窗"
            setOnClickListener {
                 Dialog.build(ctx) {
                     progress("测试Progress弹窗")
                     cancelable(true)
                     show()
                }
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "消息弹窗"
            setOnClickListener {
                Dialog.build(ctx) {
                    message("测试消息弹窗", "消息消息消息消息消息消息消息消息消息")
                    positiveButton("右侧按钮", action = { dialog ->
                        showHint("点击了右侧消息按钮")
                        dialog?.dismiss()
                    })
                    show()
                }
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "列表弹窗"
            setOnClickListener {
                Dialog.build(ctx) {
                    items("测试列表弹窗",
                            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"),
                            onClick = { dialog, item, position ->
                                showHint("点击了$item")
                            })
                    positiveButton("右侧按钮", action = { dialog ->
                        showHint("点击了右侧按钮")
                        dialog?.dismiss()
                    })
                    neutralButton("中间按钮", action = { showHint("点击了中间按钮")})
                    negativeButton("左侧按钮", action = { showHint("点击了左侧按钮")})
                    cancelable(false)
                    show()
                }
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "单选弹窗"
            setOnClickListener {
                Dialog.build(ctx) {
                    singleChoiceItems("测试单选弹窗",
                            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"),
                            emptyList(),
                            5,
                            onChecked = { item, position ->
                                showHint("点击了$item")
                            })
                    positiveButton("右侧按钮", action = { dialog ->
                        showHint("点击了右侧按钮")
                        dialog?.dismiss()
                    })
                    neutralButton("中间按钮", action = { showHint("点击了中间按钮")})
                    negativeButton("左侧按钮", action = { showHint("点击了左侧按钮")})
                    cancelable(false)
                    show()
                }
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "多选弹窗"
            setOnClickListener {
                Dialog.build(ctx) {
                    val list: MutableList<Int> = ArrayList()
                    list.add(2); list.add(4); list.add(6)
                    multiChoiceItems("测试单选弹窗",
                            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"),
                            listOf("1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999", "1010", "1111", "1212"),
                            list,
                            onChecked = { item, positions ->
                                showHint("点击了$item")
                                logi { "多选：$positions" }
                            })
                    positiveButton("右侧按钮", action = { dialog ->
                        showHint("点击了右侧按钮")
                        dialog?.dismiss()
                    })
                    neutralButton("中间按钮", action = { showHint("点击了中间按钮")})
                    negativeButton("左侧按钮", action = { showHint("点击了左侧按钮")})
                    cancelable(false)
                    show()
                }
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "查看帖子详情"
            setOnClickListener {
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "查看收到消息"
            setOnClickListener {
            }
        })
        layout_debug_container.addView(Button(ctx).apply {
            text = "查看我的帖子"
            setOnClickListener {
            }
        })
    }
}