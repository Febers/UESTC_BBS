/*
 * Created by Febers at 18-8-16 下午9:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 下午9:38.
 */

package com.febers.uestc_bbs.base


import android.os.Bundle
import android.support.annotation.Nullable
import android.support.annotation.UiThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.view.custom.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

const val FID = "mFid"
const val UID = "mUid"
const val MSG_TYPE = "mMsgType"

abstract class BaseFragment : SupportFragment(), BaseView {

    protected var mFid: Int = 0
    protected var mUid: Int = 0
    protected var mMsgType: String? = MSG_TYPE_REPLY

    protected abstract fun setContentView():Int

    protected open fun registerEventBus(): Boolean = false

    protected open fun initView() {}

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        arguments?.let {
            mFid = it.getInt(FID, 0)
            mUid = it.getInt(UID, 0)
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

    @UiThread
    override fun showToast(msg: String) {
        context!!.toast(msg)
    }
}