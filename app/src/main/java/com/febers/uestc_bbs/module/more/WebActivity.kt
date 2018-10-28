package com.febers.uestc_bbs.module.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.i
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.URL

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        val url = intent.getStringExtra(URL)
        i("BA", url)
    }
}
