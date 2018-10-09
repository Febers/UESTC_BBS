package com.febers.uestc_bbs.module.message.view

import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMDetailBean.BodyBean.PmListBean
import com.febers.uestc_bbs.module.message.presenter.MessageContract
import com.febers.uestc_bbs.module.message.presenter.PMDetailPresenterImpl
import com.febers.uestc_bbs.utils.PMTimeUtils
import com.febers.uestc_bbs.view.adapter.PMDetailAdapter
import kotlinx.android.synthetic.main.activity_private_detail.*

/**
 * 私信消息的详情
 * 通过mvp模式获取历史消息
 * 通过service实时更新
 */
class PMDetailActivity : BaseSwipeActivity(), MessageContract.PMView {

    private val pmList: MutableList<PmListBean.MsgListBean> = ArrayList()
    private lateinit var pmPresenter: MessageContract.PMPresenter
    private lateinit var pmAdapter: PMDetailAdapter
    private var uid = "0"
    private var userName = ""
    private var page = 1

    override fun setToolbar(): Toolbar? {
        return toolbar_private_detail
    }

    override fun setView(): Int {
        uid = intent.getStringExtra(USER_ID)
        userName = intent.getStringExtra(USER_NAME) ?: "私信详情"
        return R.layout.activity_private_detail
    }

    override fun initView() {
        toolbar_private_detail.title = userName
        pmPresenter = PMDetailPresenterImpl(this)
        pmAdapter = PMDetailAdapter(this, pmList, MyApplication.getUser().uid.toInt(), PMTimeUtils())
        recyclerview_private_detail.apply {
            adapter = pmAdapter
        }
        getPmList()
    }

    private fun getPmList() {
        pmPresenter.pmRequest(uid = uid, page = page)
    }

    /**
     * 将数据显示在界面上
     * recyclerview要移动到底部
     * 添加新数据的时候从底部添加
     * pmAdapter.notifyItemInserted(pmList.size-1)
     * recyclerview_private_detail.scrollToPosition(pmList.size-1)
     */
    override fun showPMDetail(event: BaseEvent<PMDetailBean>) {
        event.data.body?.pmList?.get(0)?.msgList?.let { pmList.addAll(it) }
        pmAdapter.notifyItemInserted(pmList.size-1)
        scroll_view_pm.post {
            scroll_view_pm.scrollTo(0, linear_layout_pm_content.measuredHeight)
        }
    }
}
