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
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

const val FID = "fid"

abstract class BaseFragment : SupportFragament(), BaseView {

    protected var fid: String? = null

    protected abstract fun setContentView():Int

    protected open fun registerEventBus(): Boolean = false

    protected open fun initView() {}

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        arguments?.let {
            fid = it.getString(FID)
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    @UiThread
    override fun onError(error: String) {
        BaseApplication.context().toast(error)
    }
}