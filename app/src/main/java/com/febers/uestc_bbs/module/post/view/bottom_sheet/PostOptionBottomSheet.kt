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
import kotlinx.android.synthetic.main.layout_bottom_sheet_post_option.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.toast

const val ITEM_WEB_POST = 0
const val ONLY_AUTHOR_REPLY = 1
const val ITEM_ORDER_POSITIVE = 2
const val ITEM_COPY_URL = 3
const val ITEM_SHARE_POST = 4

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
            if (i == ITEM_WEB_POST) {
                itemClickListenerPost.onOptionItemSelect(ITEM_WEB_POST)
            }
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
            if (i == ITEM_COPY_URL) {
                val clipboardManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("uestc_bbs_clip_data", postId.pidToWebUrl())
                clipboardManager.setPrimaryClip(clipData)
                context.toast(context.getString(R.string.copy_url_successfully))
            }
            if (i == ITEM_SHARE_POST) {
                context.share("$postTitle\n${postId.pidToWebUrl()}\n来自清水河畔客户端 i河畔")
            }
            dismiss()
        }
        recyclerview_post_option.adapter = optionItemAdapter
    }

    private fun getOptionList(): List<OptionItemBean> {
        return arrayListOf(
                OptionItemBean(context.getString(R.string.web_page), R.drawable.xic_web_purple_24dp),
                OptionItemBean(if (authorOnly)context.getString(R.string.view_all) else context.getString(R.string.view_only_author),
                        if (authorOnly)R.drawable.xic_people_blue_24dp else R.drawable.xic_person_blue_24dp),
                OptionItemBean(if (orderPositive)context.getString(R.string.reverse_view) else context.getString(R.string.positive_order_view),
                        if (orderPositive)R.drawable.xic_order_navi_green_24dp else R.drawable.xic_order_posi_green_24dp),
                OptionItemBean(context.getString(R.string.copy_url), R.drawable.xic_link_orange_24dp),
                OptionItemBean(context.getString(R.string.share), R.drawable.xic_share_blue_24dp))
    }
}

fun Int.pidToWebUrl() = "http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=${this}"