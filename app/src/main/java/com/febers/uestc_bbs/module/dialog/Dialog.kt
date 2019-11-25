package com.febers.uestc_bbs.module.dialog

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.febers.uestc_bbs.R
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

    fun items(title: String, items: List<String>, onClick: (dialog: AlertDialog?, item: String, position: Int) -> Unit) {
        contextRef.get() ?: return
        val layout = LayoutInflater.from(contextRef.get()).inflate(R.layout.dialog_list, null)
        val listView = layout.findViewById<ListView>(R.id.list_view_dialog)
        val tvTitle = layout.findViewById<TextView>(R.id.list_dialog_title)
        btnLeft = layout.findViewById<Button>(R.id.btn_list_dialog_left)
        btnMid = layout.findViewById<Button>(R.id.btn_list_dialog_mid)
        btnRight = layout.findViewById<Button>(R.id.btn_list_dialog_right)
        tvTitle.text = title
        listView.adapter = ArrayAdapter<String>(contextRef.get()!!, android.R.layout.simple_list_item_1, items)
        listView.setOnItemClickListener { parent, view, position, id ->
            onClick(dialog, items[position], position)
        }
        builder.setView(layout)
        dialog = builder.create()
    }

    fun <T> singleChoiceItems(items: List<T>, checked: Int, onClick: (item: T, position: Int) -> Unit) {

    }

    fun <T> multiChoiceItems(items: List<T>, checked: IntArray, onClick: (item: T, position: IntArray) -> Unit) {

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