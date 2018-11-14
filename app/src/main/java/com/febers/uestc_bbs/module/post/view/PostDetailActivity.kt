package com.febers.uestc_bbs.module.post.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.entity.PostFavResultBean
import com.febers.uestc_bbs.entity.PostVoteResultBean
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.view.adapter.PostReplyItemAdapter
import com.febers.uestc_bbs.module.post.contract.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostOptionClickListener
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostOptionBottomSheet
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostReplyBottomSheet
import com.febers.uestc_bbs.module.post.view.bottom_sheet.PostReplySendListener
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.LogUtils
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToUserDetail
import com.febers.uestc_bbs.view.helper.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlin.math.absoluteValue

/**
 * TODO 此界面设计有重大bug
 * 加载视图的时候，recyclerview一次性会加载所有的图片请求
 * 因为无法获知其是否正在显示
 * 尝试将Recyclerview改为ListView
 */
class PostDetailActivity : BaseActivity(), PostContract.View, PostOptionClickListener, PostReplySendListener {

    private var replyList: MutableList<PostDetailBean.ListBean>? = ArrayList()
    private var postPresenter: PostContract.Presenter? = null
    private var replyItemAdapter: PostReplyItemAdapter? = null
    private var contentViewHelper: ContentViewHelper? = null
    private var voteViewHelper: VoteViewHelper? = null
    private var optionBottomSheet: PostOptionBottomSheet? = null
    private var replyBottomSheet: PostReplyBottomSheet? = null
    private var tempReplyBean: PostDetailBean.ListBean? = null
    private var isFavorite: Int = POST_NO_FAVORED
    private var postOrder = POST_POSITIVE_ORDER
    private val delFavoritePost = "delfavorite"
    private val favoritePost = "favorite"
    private var isInsertReplySimply = false
    private var drawFinish = false
    private var topicName = "楼主"  //楼主名称
    private var topicReplyId = 0
    private var replyCount = 0
    private var authorId = 0
    private var topicId = 0 //楼主id
    private var postId = 0
    private var page = 1


    ////////////////////////////////初始化////////////////////////////////

    override fun setView(): Int = R.layout.activity_post_detail
    override fun setToolbar(): Toolbar? = toolbar_post_detail
    override fun setMenu(): Int? = R.menu.menu_post_detail

    override fun initView() {
        postId = intent.getIntExtra(FID, 0)
        postPresenter = PostPresenterImpl(this)
        replyItemAdapter = PostReplyItemAdapter(this, replyList!!, false).apply {
            setOnItemClickListener { viewHolder, postReplyBean, i ->
                getReplyBottomSheet().showWithData(topicId = topicId, toUid = postReplyBean.reply_id,
                        replyId = postReplyBean.reply_posts_id, toUName = postReplyBean.reply_name!!) }
            setOnItemChildClickListener(R.id.image_view_post_reply_author_avatar) {
                viewHolder, postReplyBean, i -> clickToUserDetail(this@PostDetailActivity, postReplyBean.reply_id)
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
            layoutManager = LinearLayoutManager(this@PostDetailActivity).apply {
                stackFromEnd = true //配合adjustResize使软键盘弹出时recyclerview不错乱
            }
            addItemDecoration(DividerItemDecoration(this@PostDetailActivity, LinearLayoutManager.VERTICAL))
            adapter = replyItemAdapter
        }

//        FABBehaviorHelper.fabBehaviorWithScrollView(scroll_view_post_detail, fab_post_detail)
        scroll_view_post_detail.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

            replyItemAdapter?.isListScrolling = (scrollY - oldScrollY).absoluteValue >= 30

            if (scrollY - oldScrollY < -3) {
                fab_post_detail.show()
            }
            if (scrollY - oldScrollY > 3) {
                fab_post_detail.hide()
            }
        }
        scroll_view_post_detail.isNestedScrollingEnabled = true
    }

    /*
    获取数据之后,恢复加载更多设置
    (由于已经关闭加载更多，只能由刷新触发)，
     */
    private fun getPost(postId: Int, page: Int, authorId: Int = this.authorId, order: Int = this.postOrder) {
        refresh_layout_post_detail.setNoMoreData(false)
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
        }
        //如果是投票贴
        if (event.data.topic?.vote == POST_IS_VOTE && event.data.topic?.poll_info != null) {
            drawVoteView(event.data.topic?.poll_info as PostDetailBean.TopicBean.PollInfoBean)
        }
        topicId = event.data.topic?.user_id ?: topicId //倒叙查看中其值可能为null
        topicReplyId = event.data.topic?.reply_posts_id ?: topicReplyId
        topicName = event.data.topic?.user_nick_name ?: topicName
        replyCount = event.data.topic?.replies ?: replyCount
        //结束绘制
        drawFinish = true
        fab_post_detail?.setOnClickListener {
            getReplyBottomSheet().showWithData(topicId = topicId, toUid = topicId,
                    replyId = topicReplyId, toUName = topicName)
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
        contentViewHelper = ContentViewHelper(linear_layout_detail_content, event.data.topic?.content!!)
        contentViewHelper?.create()
        fillImageView()
        //由于只有第一页时才绘制主贴内容，相当于刷新，所以清空评论列表
        replyList?.clear()
        //将帖子标题传递给BottomSheet以便进行后续的复制与分享工作
        getOptionBottomSheet().postTitle = event.data.topic?.title!!
    }

    //调用ImageLoader预加载的图片填充到view中
    private fun fillImageView() {
        contentViewHelper?.getImageUrls()?.forEachIndexed { index, s ->
            contentViewHelper?.getImageViews()?.get(index).apply {
                ImageLoader.usePreload(context = this?.context, url = contentViewHelper?.getImageUrls()?.get(index), imageView = this)
            }
        }
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
    }

    @UiThread
    override fun showVoteResult(event: BaseEvent<PostVoteResultBean>) {
        showToast(event.data.errcode.toString())
        refresh_layout_post_detail.autoRefresh()
    }

    ////////////////////////////////收藏////////////////////////////////
    private fun initFavStatus() {
        image_view_post_fav?.let { it ->
            it.visibility = View.VISIBLE
            if (isFavorite == POST_FAVORED) {
                it.setImageResource(R.drawable.xic_star_color_24dp)
            } else {
                it.setImageResource(R.drawable.xic_star_gray_24dp)
            }
            it.setOnClickListener {
                if (isFavorite == POST_FAVORED) {
                    isFavorite = POST_NO_FAVORED
                    onFavoriteChange(POST_NO_FAVORED)
                    return@setOnClickListener
                }
                if (isFavorite == POST_NO_FAVORED){
                    isFavorite = POST_FAVORED
                    onFavoriteChange(POST_FAVORED)
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
            showToast(event.data.errcode.toString())
            if (event.code == BaseCode.SUCCESS) {
                if (isFavorite == POST_FAVORED) image_view_post_fav.setImageResource(R.drawable.xic_star_color_24dp)
                if (isFavorite == POST_NO_FAVORED) image_view_post_fav.setImageResource(R.drawable.xic_star_gray_24dp)
            }
        }
    }

    ////////////////////////////////回复////////////////////////////////
    /**
     * 接收来自bottomSheet的回调，初始化或者重新定义tempReplyBean
     * 然后发送消息
     * 此方法创建了一个temReplyBean，其目的是当用户回复的内容足够简单时
     * 服务器显示回复成功之后直接将其插入至list末尾，因为回复成功之后服务器只会返回很简单的json数据
     * 而不用再次刷新回复列表，不过其是否有必要，还不确定
     * 只有在回复的数目够小，且该回复不引用其他回复以及回复不包含图片(目前客户端并不支持这一功能)
     * isInsertReplySimply 这一变量才为true
     */
    override fun onReplySend(toUid: Int, isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>) {
        if (replyCount < COMMON_PAGE_SIZE -1 && isQuote == REPLY_NO_QUOTA) {
            tempReplyBean = PostDetailBean.ListBean().apply {
                reply_content = listOf(PostDetailBean.ContentBean().apply {
                    type = contents[0].first
                    infor = contents[0].second
                })
                reply_id = 0
                reply_name = MyApp.getUser().name
                icon = MyApp.getUser().avatar
                userTitle = MyApp.getUser().title
                posts_date = System.currentTimeMillis().toString()
                position = replyCount + 2 //adapter中会减1，因为服务器默认主贴为1楼
            }
            isInsertReplySimply = true
        }
        postPresenter?.postReplyRequest(isQuote = isQuote, replyId = replyId, contents = *contents)
        replyCount++
    }

    /**
     * 发送消息成功之后的回调
     * 如果replyCount（当前帖子的回复书）小于COMMON_PAGE_SIZE,将tempReplyBean添加到列表中
     * 否则加载下一页(?)
     * ！！效果不好，改为回复成功之后直接刷新界面
     */
    override fun showPostReplyResult(event: BaseEvent<PostSendResultBean>) {
        runOnUiThread {
            showToast(event.data.head?.errInfo.toString())
//            if (isInsertReplySimply && tempReplyBean != null) {
//                //i("Post", tempReplyBean!!.userTitle)
//                replyList.add(tempReplyBean!!)
//                replyItemAdapter.notifyDataSetChanged()
//                scroll_view_post_detail.scrollTo(0, linear_layout_post_detail.height)
//            } else {
//                //i("Post", "load")
//                scroll_view_post_detail.scrollTo(0, linear_layout_post_detail.height)
//                refresh_layout_post_detail?.autoLoadMore()
//            }
            replyBottomSheet?.finish()
            scroll_view_post_detail.scrollTo(0, 0) //移动至顶部
            refresh_layout_post_detail?.autoRefresh()
        }
    }

    ////////////////////////////////底部菜单////////////////////////////////
    override fun onOptionItemSelect(position: Int) {
        if (position == ONLY_AUTHOR) { //只看楼主
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_post_detail_option && drawFinish) {
            getOptionBottomSheet().show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getOptionBottomSheet(): PostOptionBottomSheet {
        if (optionBottomSheet == null) {
            optionBottomSheet = PostOptionBottomSheet(context = this, style = R.style.PinkBottomSheetTheme,
                    itemClickListenerPost = this, postId = postId)
            optionBottomSheet!!.setContentView(R.layout.layout_bottom_sheet_option)
        }
        return optionBottomSheet!!
    }

    private fun getReplyBottomSheet(): PostReplyBottomSheet {
        if (replyBottomSheet == null) {
            replyBottomSheet = PostReplyBottomSheet(this, R.style.PinkBottomSheetTheme, this)
            replyBottomSheet!!.setContentView(R.layout.layout_bottom_sheet_reply)
        }
        return replyBottomSheet!!
    }

    ////////////////////////////////错误////////////////////////////////
    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_post_detail?.finishFail()
        drawFinish = true
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
        contentViewHelper = null
        voteViewHelper = null
        tempReplyBean = null
        replyList = null
        optionBottomSheet = null
        replyBottomSheet = null
    }
}
