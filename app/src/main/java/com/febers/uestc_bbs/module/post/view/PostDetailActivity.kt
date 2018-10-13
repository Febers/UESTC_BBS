package com.febers.uestc_bbs.module.post.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.support.annotation.UiThread
import android.support.design.widget.BottomSheetDialog
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
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostOptionClickListener
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostOptionBottomSheet
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostReplyBottomSheet
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.view.utils.ContentViewHelper
import com.febers.uestc_bbs.view.utils.FABBehaviorHelper
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : BaseSwipeActivity(), PostContract.View, PostOptionClickListener {

    private var replyList: MutableList<PostDetailBean.ListBean> = ArrayList()
    private lateinit var postPresenter: PostContract.Presenter
    private lateinit var replyItemAdapter: PostReplyItemAdapter
    private lateinit var optionBottomSheet: PostOptionBottomSheet
    private lateinit var replyBottomSheet: BottomSheetDialog
    private lateinit var hideFAB: AnimatorSet
    private lateinit var showFAB: AnimatorSet
    private var page = 1
    private var authorId = 0
    private var topicId = 0
    private var postOrder = POST_POSITIVE_ORDER
    private var postId: Int = 0
    private var replyOrder = POST_POSITIVE_ORDER
    private var isFavorite: Int = POST_NO_FAVORED
        set(value) {
            field = value
            onFavoriteChange(field)
        }
    private var drawFinish: Boolean = false

    override fun setView(): Int = R.layout.activity_post_detail
    override fun setToolbar(): Toolbar? =toolbar_post_detail
    override fun setMenu(): Int? = R.menu.menu_post_detail

    override fun initView() {
        postId = intent.getIntExtra(FID, 0)
        postPresenter = PostPresenterImpl(this)
        replyItemAdapter = PostReplyItemAdapter(this, replyList, false).apply {
            setOnItemClickListener { viewHolder, postReplyBean, i ->  }
            setOnItemChildClickListener(R.id.image_view_post_reply_author_avatar) {
                viewHolder, postReplyBean, i -> ViewClickUtils.clickToUserDetail(this@PostDetailActivity, postReplyBean.reply_id)
            }
        }

        optionBottomSheet = PostOptionBottomSheet(context = this, style = R.style.PinkBottomSheetTheme,
                itemClickListenerPost = this, postId = postId)
        optionBottomSheet.setContentView(R.layout.layout_bottom_sheet_option)
        replyBottomSheet = PostReplyBottomSheet(this, R.style.PinkBottomSheetTheme)
        replyBottomSheet.setContentView(R.layout.layout_bottom_sheet_reply)

        refresh_layout_post_detail.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                drawFinish = false
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
        FABBehaviorHelper.fabBehaviorWithScrollView(scroll_view_post_detail, fab_post_detail)
    }

    /*
    获取数据之后,恢复加载更多设置
    (由于已经关闭加载更多，只能由刷新触发)，
     */
    private fun getPost(postId: Int, page: Int, authorId: Int = this.authorId, order: Int = this.postOrder) {
        refresh_layout_post_detail.setNoMoreData(false)
        postPresenter.postRequest(postId, page, authorId, order)
    }

    /**
     * 接收来自presenter的消息，绘制帖子视图
     * 首先如果不是第一页，只添加主贴视图
     * 然后绘制评论列表
     */
    @SuppressLint("SetTextI18n")
    @UiThread
    override fun showPost(event: BaseEvent<PostDetailBean>) {
        refresh_layout_post_detail?.apply {
            finishLoadMore(true)
            finishRefresh(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            drawTopicView(event)
        }
        replyList.addAll(event.data.list!!)
        replyItemAdapter.notifyDataSetChanged()
        //如果没有下一页
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_detail?.finishLoadMoreWithNoMoreData()
        }
        //如果是投票贴
        if (event.data.topic?.vote == POST_IS_VOTE && event.data.topic?.poll_info != null) {
            drawVoteView(event.data.topic?.poll_info as PostDetailBean.TopicBean.PollInfoBean)
        }
        topicId = event.data.topic?.user_id ?: topicId //倒叙查看中其值可能为null
        //结束绘制
        drawFinish = true
    }

    /**
     * 绘制主贴视图
     */
    private fun drawTopicView(event: BaseEvent<PostDetailBean>) {
        linear_layout_detail_divide?.visibility = View.VISIBLE
        image_view_post_detail_author_avatar?.let { it ->
            it.visibility = View.VISIBLE
            ImageLoader.load(this, event.data.topic?.icon, it, isCircle = true)
            it.setOnClickListener { ViewClickUtils.clickToUserDetail(this@PostDetailActivity, event.data.topic?.user_id) }
        }
        //收藏图标的相应设置
        isFavorite = event.data.topic?.is_favor!!
        initFavStatus()
        toolbar_post_detail?.title = event.data.forumName
        text_view_post_detail_title?.text = event.data.topic?.title
        text_view_post_detail_author?.text = event.data.topic?.user_nick_name
        text_view_post_detail_author_title?.text = event.data.topic?.userTitle
        text_view_post_detail_date?.text = TimeUtils.stampChange(event.data.topic?.create_date)
        //主贴图文视图的绘制
        ContentViewHelper.create(linear_layout_detail_content, event.data.topic?.content)
        fillImageView()
        //由于只有第一页时才绘制主贴内容，相当于刷新，所以清空评论列表
        replyList.clear()
        //将帖子标题传递给BottomSheet以便进行后续的复制与分享工作
        optionBottomSheet.postTitle = event.data.topic?.title!!
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

    //调用ImageLoader预加载的图片填充到view中
    private fun fillImageView() {
        ContentViewHelper.getImageUrls().forEachIndexed { index, s ->
            ContentViewHelper.getImageViews()[index].apply {
                ImageLoader.usePreload(context = context, url = ContentViewHelper.getImageUrls()[index], imageView = this)
            }
        }
    }

    private fun initFavStatus() {
        image_view_post_fav?.let { it ->
            it.visibility = View.VISIBLE
            if (isFavorite == POST_FAVORED) {
                it.setImageResource(R.drawable.ic_star_color_24dp)
            } else {
                it.setImageResource(R.drawable.ic_star_gray_24dp)
            }
            it.setOnClickListener {
                if (isFavorite == POST_FAVORED) {
                    image_view_post_fav.setImageResource(R.drawable.ic_star_gray_24dp)
                    isFavorite = POST_NO_FAVORED
                    return@setOnClickListener
                }
                if (isFavorite == POST_NO_FAVORED){
                    image_view_post_fav.setImageResource(R.drawable.ic_star_color_24dp)
                    isFavorite = POST_FAVORED
                }
            }
        }
    }

    private fun onFavoriteChange(isFav: Int) {
        //进行收藏状态的改变处理
    }

    override fun onOptionItemSelect(position: Int) {
        if (position == ITEM_AUTHOR_ONLY) { //只看楼主
            authorId = if (authorId == topicId) {
                0
            } else {
                topicId
            }
            refresh_layout_post_detail.autoRefresh()
        }
        if (position == ITEM_ORDER_POSITIVE) { //颠倒顺序
            postOrder = if (postOrder == POST_POSITIVE_ORDER){
                POST_NEGATIVE_ORDER
            } else {
                POST_POSITIVE_ORDER
            }
            refresh_layout_post_detail.autoRefresh()
        }
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
        if (item?.itemId == R.id.menu_item_post_detail_option && drawFinish) {
            optionBottomSheet.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
