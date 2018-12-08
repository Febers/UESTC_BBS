package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.REPLY_QUOTA
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.view.helper.ContentViewHelper
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostReplyItemAdapter(val context: Context, data: List<PostDetailBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<PostDetailBean.ListBean>(context, data, isLoadMore) {



    override fun convert(p0: ViewHolder?, p1: PostDetailBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_post_reply_author, p1?.reply_name)
        p0?.setText(R.id.text_view_post_reply_date, TimeUtils.stampChange(p1?.posts_date))
        p0?.setText(R.id.text_view_post_reply_user_title, p1?.userTitle)
        p0?.setText(R.id.text_view_post_reply_floor, "#"+(p1?.position?.minus(1)))
        var contentViewHelper: ContentViewHelper? = ContentViewHelper(
                linearLayout = p0?.convertView?.findViewById(R.id.linear_layout_post_reply)!!,
                mContents = p1?.reply_content!!)
        contentViewHelper?.create()
        if (p1.is_quote == REPLY_QUOTA) {
            p0?.setVisibility(R.id.linear_layout_post_reply_quota, View.VISIBLE)
            p0?.setText(R.id.text_view_post_reply_quota, p1.quote_content?.multiLineSpaces())
        }
        ImageLoader.load(context, p1.icon, p0?.getView(R.id.image_view_post_reply_author_avatar))

        contentViewHelper?.getImageMapList()?.forEach {
            ImageLoader.loadForContent(context = context,
                    url = it.keys.first(),
                    imageView = it.values.first())
        }
        contentViewHelper = null
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
//            if (field) GlideApp.with(context).pauseRequests()
//            else GlideApp.with(context).resumeRequests()
//        }

    //将引用的回复中的前缀跟内容分得更开
    private fun String.multiLineSpaces(): String = replace("\n", "\n\n")
}
