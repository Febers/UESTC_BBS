package com.febers.uestc_bbs.module.post.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.UiThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import androidx.core.widget.NestedScrollView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.entity.PostFavResultBean
import com.febers.uestc_bbs.entity.PostVoteResultBean
import com.febers.uestc_bbs.view.adapter.PostReplyItemAdapter
import com.febers.uestc_bbs.module.post.contract.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostOptionClickListener
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostOptionBottomSheet
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.post.view.edit.POST_REPLY_RESULT
import com.febers.uestc_bbs.module.post.view.edit.POST_REPLY_RESULT_CODE
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.context.ClickContext.clickToUserDetail
import com.febers.uestc_bbs.module.post.view.bottom_sheet.pidToWebUrl
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.helper.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.layout_server_null.*
import org.jetbrains.anko.browse

class PostDetailActivity : BaseActivity(), PostContract.View, PostOptionClickListener {

    private var replyList: MutableList<PostDetailBean.ListBean>? = ArrayList()
    private var showReplyCountBottomAnimatorSet: AnimatorSet? = null
    private var hideReplyCountBottomAnimatorSet: AnimatorSet? = null
    private var postPresenter: PostContract.Presenter? = null
    private var replyItemAdapter: PostReplyItemAdapter? = null
    private var contentViewHelper: ContentViewHelper? = null
    private var voteViewHelper: VoteViewHelper? = null
    private var optionBottomSheet: PostOptionBottomSheet? = null
    private var isFavorite: Int = POST_NO_FAVORED
    private var postOrder = POST_POSITIVE_ORDER
    private val delFavoritePost = "delfavorite"
    private val favoritePost = "favorite"
    private var drawFinish = false
    private var topicUserName = "楼主"  //楼主名称
    private var topicReplyId = 0
    private var replyCount = 0
    private var authorId = 0
    private var topicUserId = 0 //楼主id
    private var postId = 0
    private var page = 1

    ////////////////////////////////初始化////////////////////////////////

    override fun setView(): Int = R.layout.activity_post_detail
    override fun setToolbar(): Toolbar? = toolbar_post_detail
    override fun setMenu(): Int? = R.menu.menu_post_detail

    override fun afterCreated() {
        postId = intent.getIntExtra(FID, 0)
        postPresenter = PostPresenterImpl(this)
        replyItemAdapter = PostReplyItemAdapter(this, replyList!!).apply {
            setOnItemChildClickListener(R.id.image_view_post_reply_author_avatar) {
                viewHolder, postReplyBean, i -> clickToUserDetail(mContext, postReplyBean.reply_id)
            }
            setOnItemChildClickListener(R.id.image_view_post_reply_reply) {
                viewHolder, postReplyBean, i ->
                ClickContext.clickToPostReply(context = mContext,
                        toUserId = postReplyBean.reply_id,
                        toUserName = postReplyBean.reply_name,
                        postId = postId,
                        replyId = postReplyBean.reply_posts_id,
                        isQuota = true,
                        replySimpleDescription = postReplyBean.reply_content?.get(0)?.infor.toString())
            }
        }

        refresh_layout_post_detail.apply {
            initAttrAndBehavior()
            setOnRefreshListener {
                drawFinish = false
                page = 1
                getPost(postId, page) }
            setOnLoadMoreListener {
                getPost(postId, ++page) }
        }
        recyclerview_post_detail_replies.apply {
            layoutManager = LinearLayoutManager(mContext).apply {
                stackFromEnd = true //配合adjustResize使软键盘弹出时recyclerView不错乱
            }
            //addItemDecoration(DividerItemDecoration(this@PostDetailActivity, LinearLayoutManager.VERTICAL))
            adapter = replyItemAdapter
        }

        scroll_view_post_detail.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY - oldScrollY < -10) {
                showReplyCountBottomAndFAB()
            }
            if (scrollY - oldScrollY > 10) {
                hideReplyCountBottomAndFAB()
            }
        }
        fab_post_detail.setOnClickListener {
            scroll_view_post_detail.scrollTo(0, 0)
        }
    }

    override fun initView() {
    }

    /*
    获取数据之后,恢复加载更多设置
    (由于已经关闭加载更多，只能由刷新触发)，
     */
    private fun getPost(postId: Int, page: Int, authorId: Int = this.authorId, order: Int = this.postOrder) {
        refresh_layout_post_detail.setNoMoreData(false)
        tv_bottom_hint.visibility = View.INVISIBLE
        postPresenter?.postDetailRequest(postId, page, authorId, order)
    }

    ////////////////////////////////主贴////////////////////////////////
    /**
     * 接收来自presenter的消息，绘制帖子视图
     * 首先如果不是第一页，只添加主贴视图
     * 然后绘制评论列表
     */
    @SuppressLint("SetTextI18n")
    @UiThread
    override fun showPostDetail(event: BaseEvent<PostDetailBean>) {
        if (page == 1) {
            drawTopicView(event)
        }
        replyList?.addAll(event.data.list!!)
        replyItemAdapter?.notifyDataSetChanged()
        //如果没有下一页
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_detail?.finishLoadMoreWithNoMoreData()
            tv_bottom_hint.postDelayed( { tv_bottom_hint.visibility = View.VISIBLE }, 500)

        }
        //如果是投票贴
        if (event.data.topic?.vote == POST_IS_VOTE && event.data.topic?.poll_info != null) {
            drawVoteView(event.data.topic?.poll_info as PostDetailBean.TopicBean.PollInfoBean)
        }
        topicUserId = event.data.topic?.user_id ?: topicUserId //倒叙查看中其值可能为null
        topicReplyId = event.data.topic?.reply_posts_id ?: topicReplyId
        topicUserName = event.data.topic?.user_nick_name ?: topicUserName
        replyCount = event.data.topic?.replies ?: replyCount
        //结束绘制
        drawFinish = true
        //底部显示评论个数的bottom，以代替fab
        text_view_post_reply_count.text = "$replyCount " + getString(R.string.replies)
        linear_layout_post_reply_count.setOnClickListener {
            val description: String = try {
                event.data.topic?.content?.get(0)?.infor + " "
            } catch (e: Exception) {
                "[主贴内容]"
            }
            ClickContext.clickToPostReply(context = mContext,
                    toUserId = topicUserId,
                    toUserName = topicUserName,
                    postId = postId,
                    replyId = topicReplyId,
                    isQuota = false,
                    replySimpleDescription = description)
        }
        refresh_layout_post_detail?.finishSuccess()
    }

    /**
     * 绘制主贴视图
     */
    private fun drawTopicView(event: BaseEvent<PostDetailBean>) {
        linear_layout_detail_divide?.visibility = View.VISIBLE
        image_view_post_detail_author_avatar?.let { it ->
            it.visibility = View.VISIBLE
            ImageLoader.load(this, event.data.topic?.icon, it, isCircle = true)
            it.setOnClickListener { clickToUserDetail(mContext, event.data.topic?.user_id) }
        }
        //收藏图标的相应设置
        isFavorite = event.data.topic?.is_favor ?: POST_NO_FAVORED
        initFavStatus()
        toolbar_post_detail?.title = event.data.forumName
        text_view_post_detail_title?.text = event.data.topic?.title
        text_view_post_detail_author?.text = event.data.topic?.user_nick_name
        text_view_post_detail_author_title?.text = event.data.topic?.userTitle
        text_view_post_detail_date?.text = TimeUtils.stampChange(event.data.topic?.create_date)
        //主贴图文视图的绘制
        contentViewHelper = ContentViewHelper(linear_layout_detail_content, event.data.topic?.content!!)
        contentViewHelper?.create()
        fillImageView()
        //由于只有第一页时才绘制主贴内容，相当于刷新，所以清空评论列表
        replyList?.clear()
        //将帖子标题传递给BottomSheet以便进行后续的复制与分享工作
        getOptionBottomSheet().postTitle = event.data.topic?.title!!
    }

    private fun fillImageView() {
        val imageMap = contentViewHelper?.getImageMapList()
        imageMap ?: return
        imageMap.forEach {
            ImageLoader.loadForContent(context = mContext,
                    url = it.keys.first(),
                    imageView = it.values.first())
        }
        contentViewHelper = null
    }

    ////////////////////////////////投票////////////////////////////////
    /**
     * 绘制投票的界面
     * 包括用户未投票的，由RadioGroup和Button组成的界面
     * 以及自定义的投票结果View
     * 该投票结果View应该由ListView构成
     * @param pollInfo 投票详数据情
     */
    private fun drawVoteView(pollInfo: PostDetailBean.TopicBean.PollInfoBean?) {
        pollInfo ?: return
        voteViewHelper = VoteViewHelper(linear_layout_detail_content, pollInfo)
        voteViewHelper?.create()
        voteViewHelper?.setVoteButtonClickListener(object : VoteViewHelper.VoteButtonClickListener {
            override fun click(pollItemIds: List<Int>) {
                postPresenter?.postVoteRequest(pollItemIds)
            }
        })
        voteViewHelper = null
    }

    @UiThread
    override fun showVoteResult(event: BaseEvent<PostVoteResultBean>) {
        showHint(event.data.errcode.toString())
        refresh_layout_post_detail.autoRefresh()
    }

    ////////////////////////////////收藏////////////////////////////////
    private fun initFavStatus() {
        image_view_post_fav?.let { it ->
            it.visibility = View.VISIBLE
            if (isFavorite == POST_FAVORED) {
                it.setImageResource(R.drawable.xic_star_color_24dp)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    it.drawable.setTint(ThemeHelper.getColorPrimaryBySp())
                }
            } else {
                it.setImageResource(R.drawable.xic_star_gray_24dp)
            }
            it.setOnClickListener {
                if (isFavorite == POST_FAVORED) {
                    isFavorite = POST_NO_FAVORED
                    onFavoriteChange(POST_NO_FAVORED)
                    showHint(getString(R.string.cancel_collect))
                    return@setOnClickListener
                }
                if (isFavorite == POST_NO_FAVORED){
                    isFavorite = POST_FAVORED
                    onFavoriteChange(POST_FAVORED)
                    showHint(getString(R.string.collecting))
                }
            }
        }
    }

    //进行收藏状态改变的后台处理
    //不使用全局的isFavorite，防止频繁点击时逻辑跟不上
    private fun onFavoriteChange(isFav: Int) {
        postPresenter?.postFavRequest(action = if (isFav == POST_FAVORED) favoritePost else delFavoritePost)
    }

    override fun showPostFavResult(event: BaseEvent<PostFavResultBean>) {
        runOnUiThread {
            showHint(event.data.errcode.toString())
            if (event.code == BaseCode.SUCCESS) {
                if (isFavorite == POST_FAVORED){
                    image_view_post_fav.setImageResource(R.drawable.xic_star_color_24dp)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        image_view_post_fav.drawable.setTint(ThemeHelper.getColorPrimaryBySp())
                    }
                }
                if (isFavorite == POST_NO_FAVORED) image_view_post_fav.setImageResource(R.drawable.xic_star_gray_24dp)
            }
        }
    }

    ////////////////////////////////回复的结果////////////////////////////////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //launchMode会影响该功能的实现
        if (requestCode == POST_REPLY_RESULT_CODE) {
            val result = data?.getBooleanExtra(POST_REPLY_RESULT, false)
            log("result:"+result.toString())
            result ?: return
            if (result) {
                scroll_view_post_detail.scrollTo(0, 0) //移动至顶部
                refresh_layout_post_detail?.autoRefresh()
                replyCount++
            }
        }
    }

    ////////////////////////////////底部菜单////////////////////////////////
    override fun onOptionItemSelect(position: Int) {
        if (position == ONLY_AUTHOR_REPLY) { //只看楼主
            authorId = if (authorId == topicUserId) {
                0
            } else {
                topicUserId
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

    private fun getOptionBottomSheet(): PostOptionBottomSheet {
        if (optionBottomSheet == null) {
            optionBottomSheet = PostOptionBottomSheet(context = mContext, style = R.style.PinkBottomSheetTheme,
                    itemClickListenerPost = this, postId = postId)
            optionBottomSheet!!.setContentView(R.layout.layout_bottom_sheet_post_option)
        }
        return optionBottomSheet!!
    }

    private fun showReplyCountBottomAndFAB() {
        if (linear_layout_post_reply_count.visibility == View.VISIBLE) return
        linear_layout_post_reply_count.visibility = View.VISIBLE
        if (showReplyCountBottomAnimatorSet == null) {
            showReplyCountBottomAnimatorSet = AnimatorInflater.loadAnimator(mContext, R.animator.scroll_show_fab) as AnimatorSet
            (showReplyCountBottomAnimatorSet as AnimatorSet).setTarget(linear_layout_post_reply_count)
        }
        (showReplyCountBottomAnimatorSet as AnimatorSet).start()
        if (!fab_post_detail.isShown) {
            fab_post_detail.show()
        }
        //ObjectAnimator.ofFloat(linear_layout_post_reply_count, "translationY", 0f, 500f).setDuration(1000).start()
    }

    private fun hideReplyCountBottomAndFAB() {
        if (linear_layout_post_reply_count.visibility == View.INVISIBLE) return
        if (hideReplyCountBottomAnimatorSet == null) {
            hideReplyCountBottomAnimatorSet = AnimatorInflater.loadAnimator(mContext, R.animator.scroll_show_fab) as AnimatorSet
            (hideReplyCountBottomAnimatorSet as AnimatorSet).setTarget(linear_layout_post_reply_count)
        }
        (hideReplyCountBottomAnimatorSet as AnimatorSet).start()
        linear_layout_post_reply_count.visibility = View.INVISIBLE
        if (fab_post_detail.isShown) {
            fab_post_detail.hide()
        }
    }

    ////////////////////////////////顶部菜单////////////////////////////////
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_post_detail_option && drawFinish) {
            getOptionBottomSheet().show()
        }
        return super.onOptionsItemSelected(item)
    }

    ////////////////////////////////错误////////////////////////////////
    override fun showError(msg: String) {
        showHint(msg)
        refresh_layout_post_detail?.finishFail()
        showEmptyView()
        drawFinish = true
    }

    ////////////////////////////////服务器响应为null////////////////////////////////
    private fun showEmptyView() {
        toolbar_post_detail?.title = getString(R.string.unknown)
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_server_null, null)
        linear_layout_post_detail?.let {
            it.removeAllViews()
            it.addView(view)
            it.visibility = View.VISIBLE
        }
        btn_to_web?.visibility = View.VISIBLE
        btn_to_web?.setOnClickListener {
            browse(postId.pidToWebUrl(), true)
        }
    }

    /**
     * 在销毁之后尽量回收变量
     * list的clear方法是遍历所有元素并将它们赋null
     * 直接对list赋null就行了
     */
    override fun onDestroy() {
        super.onDestroy()
        postPresenter = null
        replyItemAdapter = null
        replyList = null
        optionBottomSheet = null
    }
}
