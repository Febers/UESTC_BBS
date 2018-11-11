package com.febers.uestc_bbs.utils

import android.widget.Toast
import com.febers.uestc_bbs.MyApp

object ToastUtils {

    private var toast: Toast? = null

    fun show(msg: String) {
        toast = if (toast != null) {
            (toast as Toast).cancel()
            Toast.makeText(MyApp.context(), msg, Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(MyApp.context(), msg, Toast.LENGTH_SHORT)
        }
        (toast as Toast).show()
    }
}