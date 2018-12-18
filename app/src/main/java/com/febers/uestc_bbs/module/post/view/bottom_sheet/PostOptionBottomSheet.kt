package com.febers.uestc_bbs.module.post.view.bottom_sheet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.OptionItemBean
import com.febers.uestc_bbs.view.adapter.PostOptionAdapter
import kotlinx.android.synthetic.main.layout_bottom_sheet_option.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.toast

class PostOptionBottomSheet(context: Context, style: Int,
                            private val itemClickListenerPost: PostOptionClickListener, private val postId: Int):
        BottomSheetDialog(context, style) {

    private lateinit var optionItemAdapter: PostOptionAdapter
    private var optionList: MutableList<OptionItemBean> = ArrayList()
    var postTitle: String = ""
    private var orderPositive: Boolean = true
    private var authorOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        optionList.addAll(getOptionList())
        optionItemAdapter = PostOptionAdapter(context, optionList)
        optionItemAdapter.setOnItemClickListener { viewHolder, optionItemBean, i ->
            if (i == ONLY_AUTHOR_REPLY) {
                authorOnly = !authorOnly
                optionList.clear()
                optionList.addAll(getOptionList())
                optionItemAdapter.notifyDataSetChanged()
                itemClickListenerPost.onOptionItemSelect(ONLY_AUTHOR_REPLY)
            }
            if (i == ITEM_ORDER_POSITIVE) {
                orderPositive = !orderPositive
                optionList.clear()
                optionList.addAll(getOptionList())
                optionItemAdapter.notifyDataSetChanged()
                itemClickListenerPost.onOptionItemSelect(ITEM_ORDER_POSITIVE)
            }
            if (i == ITEM_WEB_POST) {
                context.browse(postId.pidToWebUrl(), true)
                //ClickContext.clickToAppWeb(context, postId.pidToWebUrl())
            }
            if (i == ITEM_COPY_URL) {
                val clipboardManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("uestc_bbs_clip_data", postId.pidToWebUrl())
                clipboardManager.primaryClip = clipData
                context.toast("复制链接成功")
            }
            if (i == ITEM_SHARE_POST) {
                context.share("$postTitle\n${postId.pidToWebUrl()}\n来自清水河畔客户端 i河畔")
//                val intent = Intent(Intent.ACTION_SEND).apply {
//                    type = "text/plain"
//                    putExtra(Intent.EXTRA_TEXT, "$postTitle\n${postId.pidToWebUrl()}\n来自清水河畔客户端 i河畔")
//                }
//                context.startActivity(Intent.createChooser(intent, "分享帖子"))
            }
            dismiss()
        }
        recyclerview_post_option.adapter = optionItemAdapter
    }

    private fun getOptionList(): List<OptionItemBean> {
        return arrayListOf(
                OptionItemBean(if (authorOnly)"查看所有" else "只看楼主",
                        if (authorOnly)R.drawable.xic_people_blue_24dp else R.drawable.xic_person_blue_24dp),
                OptionItemBean(if (orderPositive)"倒序查看" else "正序查看",
                        if (orderPositive)R.drawable.xic_order_navi_green_24dp else R.drawable.xic_order_posi_green_24dp),
                OptionItemBean("Web页面", R.drawable.xic_web_purple_24dp),
                OptionItemBean("复制链接", R.drawable.xic_link_orange_24dp),
                OptionItemBean("分享", R.drawable.xic_share_blue_24dp))
    }

    private fun Int.pidToWebUrl() = "http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=${this}"
}