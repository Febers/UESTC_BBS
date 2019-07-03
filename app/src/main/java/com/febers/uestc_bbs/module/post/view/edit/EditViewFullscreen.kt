package com.febers.uestc_bbs.module.post.view.edit

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.febers.uestc_bbs.R

class EditViewFullscreen(private val outEditText: EditText): DialogFragment() {

    private lateinit var editText: EditText
    private lateinit var btnFinish: Button

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)

        dialog.window?.setBackgroundDrawable(ColorDrawable(0x00000000))
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.dialog_edit_view_fullscreen, container, false)

        editText = view.findViewById(R.id.edit_text_fullscreen)
        btnFinish = view.findViewById(R.id.btn_finish_edit_fullscreen)
        return view
    }

    override fun onStart() {
        super.onStart()
        editText.text = outEditText.text
        editText.requestFocus()
        btnFinish.setOnClickListener {
            outEditText.text = editText.text
            super.dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        outEditText.text = editText.text
    }

}