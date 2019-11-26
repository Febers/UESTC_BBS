package com.febers.uestc_bbs.module.dialog

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.utils.colorAccent
import java.lang.ref.WeakReference

class Dialog {

    private var dialog: AlertDialog? = null
    private lateinit var builder: AlertDialog.Builder
    private lateinit var contextRef: WeakReference<Context>

    private var btnLeft: Button? = null
    private var btnMid: Button? = null
    private var btnRight: Button? = null

    /**
     * 消息弹窗
     *
     * @param title 标题
     * @param message 消息
     */
    fun message(title: String, message: String) {
        contextRef.get() ?: return
        val layout = LayoutInflater.from(contextRef.get()).inflate(R.layout.dialog_message, null)
        val tvMessage = layout.findViewById<TextView>(R.id.text_view_message_dialog)
        val tvTitle = layout.findViewById<TextView>(R.id.text_dialog_title)
        tvMessage.text = message
        tvTitle.text = title
        btnRight = layout.findViewById<Button>(R.id.btn_msg_dialog_right)
        btnMid = layout.findViewById<Button>(R.id.btn_msg_dialog_mid)
        btnLeft = layout.findViewById<Button>(R.id.btn_msg_dialog_left)
        builder.setView(layout)
        dialog = builder.create()
    }

    /**
     * 加载中弹窗
     *
     * @param title 标题
     */
    fun progress(title: String? = contextRef.get()?.getString(R.string.loading)) {
        contextRef.get() ?: return
        val layout = LayoutInflater.from(contextRef.get()).inflate(R.layout.dialog_progress, null)
        val progressBar = layout.findViewById<ProgressBar>(R.id.pb_progress)
        val tvTitle = layout.findViewById<TextView>(R.id.progress_dialog_title)
        progressBar.indeterminateDrawable.colorFilter = PorterDuffColorFilter(colorAccent(), PorterDuff.Mode.MULTIPLY)
        title?.let {
            tvTitle.text = it
        }
        builder.setView(layout)
        dialog = builder.create()
    }

    /**
     * 列表弹窗
     *
     * @param title 标题
     * @param items 列表
     * @param onClick 点击列表项之后的回调函数
     */
    fun items(title: String, items: List<String>, onClick: (dialog: AlertDialog?, item: String, position: Int) -> Unit) {
        contextRef.get() ?: return
        val layout = LayoutInflater.from(contextRef.get()).inflate(R.layout.dialog_list, null)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.list_view_dialog)
        val tvTitle = layout.findViewById<TextView>(R.id.list_dialog_title)
        btnLeft = layout.findViewById(R.id.btn_list_dialog_left)
        btnMid = layout.findViewById(R.id.btn_list_dialog_mid)
        btnRight = layout.findViewById(R.id.btn_list_dialog_right)
        tvTitle.text = title
        recyclerView.adapter = ListAdapter(contextRef.get()!!, items).apply {
            setOnItemClickListener { viewHolder, data, position ->
                onClick(dialog, data, position)
            }
        }
        builder.setView(layout)
        dialog = builder.create()
    }

    fun singleChoiceItems(title: String, items: List<String>, descriptions: List<String>, checked: Int, onChecked: (item: String, position: Int) -> Unit) {
        contextRef.get() ?: return
        val layout = LayoutInflater.from(contextRef.get()).inflate(R.layout.dialog_list, null)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.list_view_dialog)
        val tvTitle = layout.findViewById<TextView>(R.id.list_dialog_title)
        btnLeft = layout.findViewById(R.id.btn_list_dialog_left)
        btnMid = layout.findViewById(R.id.btn_list_dialog_mid)
        btnRight = layout.findViewById(R.id.btn_list_dialog_right)
        tvTitle.text = title
        recyclerView.adapter = ChoiceAdapter(contextRef.get()!!, items, descriptions, listOf(checked))
                .apply {
                    setOnItemChildClickListener(R.id.check_item_choice_dialog) {
                    viewHolder: ViewHolder?, data: String?, position: Int ->
                        this.checked = listOf(position)
                        this.notifyDataSetChanged()
                        onChecked(data!!, position)
                }
        }

        builder.setView(layout)
        dialog = builder.create()
    }

    fun multiChoiceItems(title: String, items: List<String>, descriptions: List<String>, checked: MutableList<Int>, onChecked: (item: String, positions: IntArray) -> Unit) {
        contextRef.get() ?: return
        val layout = LayoutInflater.from(contextRef.get()).inflate(R.layout.dialog_list, null)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.list_view_dialog)
        val tvTitle = layout.findViewById<TextView>(R.id.list_dialog_title)
        btnLeft = layout.findViewById(R.id.btn_list_dialog_left)
        btnMid = layout.findViewById(R.id.btn_list_dialog_mid)
        btnRight = layout.findViewById(R.id.btn_list_dialog_right)
        tvTitle.text = title
        recyclerView.adapter = ChoiceAdapter(contextRef.get()!!, items, descriptions, checked)
                .apply {
                    setOnItemChildClickListener(R.id.check_item_choice_dialog) {
                        viewHolder: ViewHolder?, data: String?, position: Int ->
                        if (checked.contains(position)) {
                            checked.remove(position)
                        } else {
                            checked.add(position)
                        }
                        onChecked(data!!, checked.toIntArray())
                    }
                }

        builder.setView(layout)
        dialog = builder.create()
    }

    fun positiveButton(
            text: String,
            textColor: Int = colorAccent(),
            action: (dialog: AlertDialog?)->Unit
    ) {
        btnRight?.let {
            it.text = text
            it.setTextColor(textColor)
            it.setOnClickListener { action.invoke(dialog) }
            it.visibility = View.VISIBLE
        }
    }

    fun negativeButton(
            text: String,
            textColor: Int = ThemeManager.colorTextFirst(),
            action: (dialog: AlertDialog?)->Unit
    ) {
        btnLeft?.let {
            it.text = text
            it.setTextColor(textColor)
            it.setOnClickListener { action.invoke(dialog) }
            it.visibility = View.VISIBLE
        }
    }

    fun neutralButton(
            text: String,
            textColor: Int = ThemeManager.colorTextFirst(),
            action: (dialog: AlertDialog?)->Unit
    ) {
        btnMid?.let {
            it.text = text
            it.setTextColor(textColor)
            it.setOnClickListener { action.invoke(dialog) }
            it.visibility = View.VISIBLE
        }
    }

    fun cancelable(b: Boolean) {
        builder.setCancelable(b)
    }

    fun show() {
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    fun re(): AlertDialog? = dialog

    private fun start(context: Context) {
        builder = AlertDialog.Builder(context)
        contextRef = WeakReference(context)
    }

    companion object {

        private val INSTANCE = Dialog()

        fun build(context: Context, func: Dialog.() -> Unit): AlertDialog? {
            INSTANCE.start(context)
            INSTANCE.func()
            return INSTANCE.re()
        }
    }
}