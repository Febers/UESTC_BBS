package com.febers.uestc_bbs.view.helper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.view.adapter.VoteItemAdapter
import com.othershe.baseadapter.ViewHolder

/**
 * 投票贴的视图绘制，投票状态有两种：可投和不可投
 * 当可以投票的时候，为Button设置点击监听，在Activity中将投票选项发送给服务器
 * 采用RecyclerView的方式绘制投票List，动态添加进LinearLayout中
 */
const val VOTE_TYPE_SINGLE = 1
const val VOTE_STATUS_VALUABLE = 2
class VoteViewHelper(private val linearLayout: LinearLayout, private val pollInfo: PostDetailBean.TopicBean.PollInfoBean) {

    private val data: MutableList<PostDetailBean.TopicBean.PollInfoBean.PollItemListBean> = ArrayList()
    private lateinit var buttonClickListener: VoteButtonClickListener
    private lateinit var voteItemAdapter: VoteItemAdapter
    private val pollIds: MutableList<Int> = ArrayList()
    private var singleSelected: Boolean = true
    private val context = linearLayout.context
    private var currentChoosePosition = -1

    fun create() {
        data.addAll(pollInfo.poll_item_list!!)
        singleSelected = pollInfo.type == VOTE_TYPE_SINGLE
        val recyclerView: RecyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            isNestedScrollingEnabled = false
        }

        val canVote: Boolean = pollInfo.poll_status == VOTE_STATUS_VALUABLE
        voteItemAdapter = VoteItemAdapter(context, data, canVote).apply {
            setOnItemChildClickListener(R.id.check_box_vote_item) {
                viewHolder, pollItemListBean, i -> setOnItemOrChildClickListener(viewHolder, pollItemListBean, i) }
            setOnItemClickListener { viewHolder, pollItemListBean, i -> setOnItemOrChildClickListener(viewHolder, pollItemListBean, i) }
        }
        recyclerView.adapter = voteItemAdapter
        val marginLayoutParams = ViewGroup.MarginLayoutParams(recyclerView.layoutParams).apply {
            setMargins(0,20,0,0) }
        recyclerView.layoutParams = marginLayoutParams
        linearLayout.addView(recyclerView)

        val button = Button(context).apply {
            text = "投票"
            isEnabled = canVote
            setOnClickListener {
                buttonClickListener.click(pollIds)
            }
            layoutParams = ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        linearLayout.addView(button)
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL
    }

    /*
        获取item的点击状态，包括整个item和CheckBox的点击
     */
    private fun setOnItemOrChildClickListener(viewHolder: ViewHolder,
                                              pollItemListBean: PostDetailBean.TopicBean.PollInfoBean.PollItemListBean,
                                              i: Int) {
        if (pollItemListBean.isSelectd == 1) {
            //说明已经点击过，当前点击会取消勾选
            pollIds.remove(pollItemListBean.poll_item_id)
        } else {
            if (singleSelected) {
                pollIds.clear()
            }
            pollIds.add(pollItemListBean.poll_item_id)
        }
        onItemSelected(pollItemListBean, i)
    }

    /*
        进行数据的处理，然后更新视图
     */
    private fun onItemSelected(pollItemListBean: PostDetailBean.TopicBean.PollInfoBean.PollItemListBean, position: Int) {
        if (position == currentChoosePosition) return
        if (singleSelected) {
            data.forEach {
                it.isSelectd = 0
            }
        }
        pollItemListBean.isSelectd = 1
        currentChoosePosition = position
        voteItemAdapter.notifyDataSetChanged()
    }

    /*
        由activity调用的button点击回调
     */
    fun setVoteButtonClickListener(listener: VoteButtonClickListener) {
        this.buttonClickListener = listener
    }

    interface VoteButtonClickListener {
        fun click(pollItemIds: List<Int>)
    }
}