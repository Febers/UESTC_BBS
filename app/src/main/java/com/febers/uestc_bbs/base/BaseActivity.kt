package com.febers.uestc_bbs.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.febers.uestc_bbs.view.MyProgressDialog

/**
 * 抽象Activity
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    protected var mProgressDialog: MyProgressDialog? = null

    protected val contentView: Int
        get() = setView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        initView()
    }

    protected abstract fun setView(): Int

    protected abstract fun initView()

    override fun showProgressDialog(title: String) {
        if (mProgressDialog == null) {
            mProgressDialog = MyProgressDialog(this)
        }
        mProgressDialog!!.show()
    }

    override fun dismissProgressDialog() {
        if (mProgressDialog == null) {
            return
        }
        mProgressDialog!!.dismiss()
    }
}
