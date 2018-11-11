/*
 * Created by Febers at 18-8-16 下午9:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 下午9:38.
 */

package com.febers.uestc_bbs.base


import android.os.Bundle
import androidx.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.utils.ToastUtils
import com.febers.uestc_bbs.view.custom.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

const val FID = "mFid"
const val UID = "mUid"
const val TITLE = "title"
const val MSG_TYPE = "mMsgType"


abstract class BaseFragment : SupportFragment(), BaseView {

    protected var mFid: Int = 0
    protected var mUid: Int = 0
    protected var mMsgType: String? = MSG_TYPE_REPLY
    protected var mTitle: String? = "i河畔"

    protected abstract fun setContentView(): Int

    protected open fun registerEventBus(): Boolean = false

    protected open fun setMenu(): Int? = null

    protected open fun setToolbar(): Toolbar? { return null }

    protected open fun initView() {}

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        arguments?.let {
            mFid = it.getInt(FID, 0)
            mUid = it.getInt(UID, 0)
            mTitle = it.getString(TITLE)
            mMsgType = it.getString(MSG_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(setContentView(), container, false)
        if (registerEventBus()) {
            if(!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //添加toolbar点击返回
        val activity: AppCompatActivity = activity as AppCompatActivity
        activity.setSupportActionBar(setToolbar())
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        setToolbar()?.setNavigationOnClickListener { pop() }
        //添加Menu
        if (setMenu() != null) {
            setHasOptionsMenu(true)
            setToolbar()?.inflateMenu(setMenu()!!)
            setToolbar()?.title = ""
        }

        initView()
    }

    override fun onDestroy() {
        mDelegate.onDestroy()
        hideSoftInput()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    override fun showToast(msg: String) {
        context?.runOnUiThread {
            ToastUtils.show(msg)
        }
    }
}