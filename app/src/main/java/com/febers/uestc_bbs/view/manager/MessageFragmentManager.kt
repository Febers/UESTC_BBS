/*
 * Created by Febers at 18-8-14 上午1:41.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午1:41.
 */

package com.febers.uestc_bbs.view.manager

import android.support.v4.app.Fragment
import android.util.Log.d
import android.util.Log.i
import com.febers.uestc_bbs.module.message.view.AMFragment
import com.febers.uestc_bbs.module.message.view.PMFragment
import com.febers.uestc_bbs.module.message.view.RMFragment
import com.febers.uestc_bbs.module.message.view.SMFragment

object MessageFragmentManager {

    private var sAMFragment: AMFragment? = null
    private var sPMFragment: PMFragment? = null
    private var sRMFragment: RMFragment? = null
    private var sSMFragment: SMFragment? = null

    fun getInctance(position: Int) : Fragment {
        when(position) {
            0 -> {
                if (sRMFragment == null) {
                    sRMFragment = RMFragment()
                    i("messageFragmentManager", "null")
                }
                return sRMFragment as Fragment
            }
            1 -> {
                if (sPMFragment == null) {
                    sPMFragment = PMFragment()
                    i("messageFragmentManager", "null")
                }
                return sPMFragment as Fragment
            }
            2 -> {
                if (sAMFragment == null) {
                    sAMFragment = AMFragment()
                    i("messageFragmentManager", "null")
                }
                return sAMFragment as Fragment
            }
            3 -> {
                if (sSMFragment == null) {
                    sSMFragment = SMFragment()
                    i("messageFragmentManager", "null")
                }
                return sSMFragment as Fragment
            }
            else -> {
                return Fragment()
            }
        }
    }

    fun destroy() {
        sRMFragment = null
        sPMFragment = null
        sAMFragment = null
        sSMFragment = null
    }
}