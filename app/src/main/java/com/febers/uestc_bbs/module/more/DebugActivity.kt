package com.febers.uestc_bbs.module.more

import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.io.UserManager
import com.febers.uestc_bbs.module.dialog.Dialog
import com.febers.uestc_bbs.utils.HintUtils
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
        layout_debug_container.addView(Button(mContext).apply {
            text = "查看用户"
            setOnClickListener {
                tv_debug_result.text = UserManager.getAllUser().toString()
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "加载中弹窗"
            setOnClickListener {
                 Dialog.build(mContext) {
                     progress("测试Progress弹窗")
                     cancelable(true)
                     show()
                }
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "消息弹窗"
            setOnClickListener {
                Dialog.build(mContext) {
                    message("测试消息弹窗", "消息消息消息消息消息消息消息消息消息")
                    positiveButton("右侧按钮", action = { dialog ->
                        showHint("点击了右侧消息按钮")
                        dialog?.dismiss()
                    })
                    show()
                }
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "列表弹窗"
            setOnClickListener {
                Dialog.build(mContext) {
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
        layout_debug_container.addView(Button(mContext).apply {
            text = "测试单选弹窗"
            setOnClickListener {
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "测试多选弹窗"
            setOnClickListener {
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "查看帖子详情"
            setOnClickListener {
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "查看收到消息"
            setOnClickListener {
            }
        })
        layout_debug_container.addView(Button(mContext).apply {
            text = "查看我的帖子"
            setOnClickListener {
            }
        })
    }
}