package com.febers.uestc_bbs.module.post.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.support.annotation.UiThread
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.view.adapter.PostReplyItemAdapter
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.view.utils.PostContentViewUtils
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : BaseSwipeActivity(), PostContract.View, OptionClickListener {

    private var replyList: MutableList<PostDetailBean.ListBean> = ArrayList()
    private lateinit var postPresenter: PostContract.Presenter
    private lateinit var replyItemAdapter: PostReplyItemAdapter
    private lateinit var optionBottomSheet: PostOptionBottomSheet
    private lateinit var replyBottomSheet: BottomSheetDialog
    private lateinit var hideFAB: AnimatorSet
    private lateinit var showFAB: AnimatorSet
    private var page = 1
    private var authorId = ""
    private var postId: String = "0"
    private var orderPositive: Boolean = true
    private var authorOnly: Boolean = false
    private var drawFinish: Boolean = false

    override fun setView(): Int = R.layout.activity_post_detail

    override fun setToolbar(): Toolbar? {
        return toolbar_post_detail
    }

    override fun setMenu(): Int? {
        return R.menu.menu_post_detail
    }

    override fun initView() {
        postId = intent.getStringExtra(FID)
        postPresenter = PostPresenterImpl(this)
        replyItemAdapter = PostReplyItemAdapter(this, replyList, false).apply {
            setOnItemClickListener { viewHolder, postReplyBean, i ->  }
            setOnItemChildClickListener(R.id.image_view_post_reply_author_avatar) {
                viewHolder, postReplyBean, i -> ViewClickUtils.clickToUserDetail(this@PostDetailActivity, postReplyBean.reply_id.toString())
            }
        }
        optionBottomSheet = PostOptionBottomSheet(this, R.style.PinkBottomSheetTheme, this)
        replyBottomSheet = PostReplyBottomSheet(this, R.style.PinkBottomSheetTheme)
        optionBottomSheet.setContentView(R.layout.layout_bottom_sheet_option)
        replyBottomSheet.setContentView(R.layout.layout_bottom_sheet_reply)

        refresh_layout_post_detail.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                page = 1
                getPost(postId, page) }
            setOnLoadMoreListener {
                getPost(postId, ++page) }
        }
        recyclerview_post_detail_replies.apply {
            layoutManager = LinearLayoutManager(this@PostDetailActivity)
            addItemDecoration(DividerItemDecoration(this@PostDetailActivity, LinearLayoutManager.VERTICAL))
            adapter = replyItemAdapter
        }

        fab_post_detail.setOnClickListener {
            if (drawFinish) {
                replyBottomSheet.show()
            }
        }
        scroll_view_post_detail?.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY - oldScrollY < -3) {
                fab_post_detail.show()
            }
            if (scrollY - oldScrollY > 3) {
                fab_post_detail.hide()
            }
        }
    }

    /*
    获取数据之后,恢复加载更多设置
    (由于已经关闭加载更多，只能由刷新触发)，
     */
    private fun getPost(_postId: String, _page: Int, _authorId: String = "", _order: String = "") {
        refresh_layout_post_detail.setNoMoreData(false)
        postPresenter.postRequest(_postId, _page, _authorId, _order)
    }

    @SuppressLint("SetTextI18n")
    @UiThread
    override fun showPost(event: BaseEvent<PostDetailBean>) {
        refresh_layout_post_detail?.apply {
            finishLoadMore(true)
            finishRefresh(true)
            setEnableLoadMore(true)
        }
//        if (page == 1) {
            //绘制主贴视图，否则只需要添加评论内容
            linear_layout_detail_divide?.visibility = View.VISIBLE
            image_view_post_detail_author_avatar?.let { it ->
                it.visibility = View.VISIBLE
                ImageLoader.load(this, event.data.topic?.icon, it, isCircle = true)
                it.setOnClickListener { ViewClickUtils.clickToUserDetail(this@PostDetailActivity, event.data.topic?.user_id.toString()) }
            }

            image_view_post_fav?.let { it ->
                it.visibility = View.VISIBLE
                if (event.data.topic?.is_favor == POST_FAVORED) {
                    it.setImageResource(R.drawable.ic_star_color_primary_24dp)
                    it.setOnClickListener {
                        image_view_post_fav.setImageResource(R.drawable.ic_star_border_black_24dp)
                        //取消收藏
                        event.data.topic?.is_favor = POST_NO_FAVORED
                    }
                } else {
                    it.setImageResource(R.drawable.ic_star_border_black_24dp)
                    it.setOnClickListener {
                        image_view_post_fav.setImageResource(R.drawable.ic_star_color_primary_24dp)
                        //收藏
                        event.data.topic?.is_favor = POST_FAVORED
                    }
                }
            }
            toolbar_post_detail?.title = event.data.forumName
            text_view_post_detail_title?.text = event.data.topic?.title
            text_view_post_detail_author?.text = event.data.topic?.user_nick_name
            text_view_post_detail_author_title?.text = event.data.topic?.userTitle
            text_view_post_detail_date?.text = TimeUtils.stampChange(event.data.topic?.create_date)
            PostContentViewUtils.create(linear_layout_detail_content, event.data.topic?.content)
            loadImageView()
            replyList.clear()
//        }
        replyList.addAll(event.data.list!!)
        replyItemAdapter.notifyDataSetChanged()
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_detail?.finishLoadMoreWithNoMoreData()
        }
        //如果是投票贴
        if (event.data.topic?.vote == POST_IS_VOTE && event.data.topic?.poll_info != null) {
            drawVoteView(event.data.topic?.poll_info as PostDetailBean.TopicBean.PollInfoBean)
        }
        drawFinish = true
    }

    /**
     * 绘制投票的界面
     * 包括用户未投票的，由RadioGroup和Button组成的界面
     * 以及自定义的投票结果View
     * 该投票结果View应该由ListView构成
     * @param pollInfo 投票详数据情
     */
    private fun drawVoteView(pollInfo: PostDetailBean.TopicBean.PollInfoBean?) {
        pollInfo ?: return
        pollInfo.poll_item_list?.forEach {
            val radioButton = RadioButton(this).apply {
                layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, 50).apply {
                    setMargins(0, 0, 0 , 0)
                }
                text = it.name
            }
        }
        //如果投票仍有效并且用户未曾投票，Button的text显示相应的文字
        button_post_vote.apply {
            //text = ""
            visibility = View.VISIBLE
            setOnClickListener {

            }
        }
    }

    private fun loadImageView() {
        PostContentViewUtils.getImageUrls().forEachIndexed { index, s ->
            PostContentViewUtils.getImageViews()[index].apply {
                ImageLoader.usePreload(context = context, url = PostContentViewUtils.getImageUrls()[index], imageView = this)
            }
        }
    }

    override fun onOptionItemSelect(position: Int) {
        if (position == ITEM_AUTHOR_ONLY) {
            authorOnly = !authorOnly
            optionBottomSheet.hide()
        }
        if (position == ITEM_ORDER_POSITIVE) {
            orderPositive = !orderPositive
            optionBottomSheet.hide()
        }
        refresh_layout_post_detail.autoRefresh()
    }

    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_post_detail?.apply {
            finishRefresh(false)
            finishLoadMore(false)
        }
        drawFinish = true
    }

    /**
     * fab滑动消失和显示的动画，弃用
     */
    private fun initAnimation() {
        hideFAB = AnimatorInflater.loadAnimator(this, R.animator.scroll_hide_fab) as AnimatorSet
        showFAB = AnimatorInflater.loadAnimator(this, R.animator.scroll_show_fab) as AnimatorSet
        hideFAB.setTarget(fab_post_detail)
        showFAB.setTarget(fab_post_detail)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_post_detail_option && drawFinish) {
            optionBottomSheet.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
