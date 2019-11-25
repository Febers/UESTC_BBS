package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.REPLY_QUOTA
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.module.post.view.content.ContentCreator
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostReplyItemAdapter(val context: Context, data: List<PostDetailBean.ListBean>, private val topicUserName: String):
        CommonBaseAdapter<PostDetailBean.ListBean>(context, data, false) {

    private var contentCreator: ContentCreator? = null

    override fun convert(holder: ViewHolder?, data: PostDetailBean.ListBean?, position: Int) {
        data ?: return
        holder ?: return
        holder.setText(R.id.text_view_post_reply_author, data.reply_name)
        holder.setText(R.id.text_view_post_reply_date, TimeUtils.stampChange(data.posts_date))
        holder.setText(R.id.text_view_post_reply_user_title, data.userTitle)
        holder.setText(R.id.text_view_post_reply_floor, data.position.minus(1).toString())
        if (topicUserName == data.reply_name) {
            holder.setVisibility(R.id.tv_post_reply_topic_user, View.VISIBLE)
        }
        if (contentCreator == null) {
            contentCreator = ContentCreator(
                    mLinearLayout = holder.convertView?.findViewById(R.id.linear_layout_post_reply)!!,
                    mContents = data.reply_content!!)
        } else {
            contentCreator!!.reset(holder?.convertView?.findViewById(R.id.linear_layout_post_reply)!!, data?.reply_content!!)
        }

        contentCreator!!.create()

        if (data.is_quote == REPLY_QUOTA) {
            holder.setVisibility(R.id.linear_layout_post_reply_quota, View.VISIBLE)
            holder.setText(R.id.text_view_post_reply_quota, data.quote_content?.multiLineSpaces())
        }
        ImageLoader.load(context, data.icon, holder.getView(R.id.image_view_post_reply_author_avatar))

        contentCreator!!.getImageMapList().forEach {
            ImageLoader.loadForContent(context = context,
                    url = it.keys.first(),
                    urls = contentCreator!!.getImageUrlList().toTypedArray(),
                    imageView = it.values.first())
        }
    }



    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post_reply
    }

    /**
     * 在上一个版本中，为了实现用户滑动时不加载图片，设置了一个标志位
     * 当用户滑动的时候，停止所有图片加载
     * 从实际体验来看，没有必要，而且当用户回帖之后，会刷新当前页面
     * 此时也相当于滑动，调用终止加载之后，已加载出的图片也会调用占位图
     * 除非可以获取refresh的状态，所以暂时停止此功能
     */
    var isListScrolling: Boolean = false
//        set(value) {
//            field = value
//            if (field) GlideApp.with(mContext).pauseRequests()
//            else GlideApp.with(mContext).resumeRequests()
//        }

    //将引用的回复中的前缀跟内容分得更开
    private fun String.multiLineSpaces(): String = replace("\n", "\n\n")
}
