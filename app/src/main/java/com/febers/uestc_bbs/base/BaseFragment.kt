package com.febers.uestc_bbs.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.view.MyProgressDialog

abstract class BaseFragment: Fragment() {

    private var isInit = false

    private var isLoad = true

    protected var mProgressDialog: MyProgressDialog? = null

    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(setContentView(), container, false)
        isInit = true
        /**初始化的时候去加载数据 */
        isCanLoadData()
        return contentView
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCanLoadData()
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private fun isCanLoadData() {
        if (!isInit) {
            return
        }

        if (userVisibleHint) {
            lazyLoad()
            isLoad = true
        } else {
            if (isLoad) {
                stopLoad()
            }
        }
    }


    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract fun setContentView(): Int

    protected fun getContentView(): View {
        return contentView!!
    }

    protected fun <T: View> findViewById(id: Int): T {
        return  getContentView().findViewById(id) as T
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract fun lazyLoad()

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected fun stopLoad() {}

    protected fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = MyProgressDialog(context)
        }
        mProgressDialog!!.show()
    }

    protected fun hideProgressDialog() {
        if (mProgressDialog == null) {
            return
        }
        mProgressDialog!!.dismiss()
    }
}