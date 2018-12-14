package com.febers.uestc_bbs.module.post.view.bottom_sheet

import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.view.WindowManager
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.REPLY_NO_QUOTA
import com.febers.uestc_bbs.base.REPLY_QUOTA
import com.febers.uestc_bbs.entity.UploadResultBean
import com.febers.uestc_bbs.io.FileUploader
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.view.adapter.ImgGridViewAdapter
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_IMG
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_TEXT
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.layout_bottom_sheet_reply.*
import java.io.File
import java.lang.StringBuilder

/**
 * 帖子详情界面，回复的底部弹出框
 * 发送消息按钮之后之后显示progress
 *
 */
class PostReplyBottomSheet(val activity: Activity, style: Int, private val listener: PostReplySendListener):
        BottomSheetDialog(activity, style) {

    private var isQuote: Int = REPLY_NO_QUOTA
    private var toUName: String = ""
    private var replyId: Int = 0
    private var topicId: Int = 0
    private var toUid: Int = 0
    private var lastToUid = toUid

    private var imgGridViewAdapter: ImgGridViewAdapter? = null

    private var needUploadImages: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        image_view_post_reply_emoji.visibility = View.GONE
        image_view_post_reply_at.visibility = View.GONE

        image_view_post_reply_picture.setOnClickListener {
            PictureSelector.create(activity)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .isDragFrame(true)
                    .enableCrop(true)
                    .previewImage(true)
                    .compress(true)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        /*
            点击发送按钮之后发送消息
            将此后的逻辑交给调用的界面
         */
        btn_post_reply.setOnClickListener {
            val stContent: String = edit_view_post_reply.text.toString()
            if (stContent.isEmpty()) return@setOnClickListener
            isQuote = if (topicId == toUid) {
                REPLY_NO_QUOTA
            } else {
                REPLY_QUOTA
            }
            sendReply(stContent)
        }

        setOnCancelListener {
            edit_view_post_reply.clearFocus()
        }
    }

    private fun sendReply(stContent: String) {
        progress_bar_post_reply.visibility = View.VISIBLE
        val aidBuffer = StringBuilder()
        val contentList: MutableList<Pair<Int, String>> = java.util.ArrayList()
        contentList.add(CONTENT_TYPE_TEXT to stContent)
        if (needUploadImages.size == 0) {
            listener.onReplySend(toUid = toUid,
                    isQuote = isQuote,
                    replyId = replyId,
                    aid = aidBuffer.toString(),
                    contents = *contentList.toTypedArray())
            return
        }
        for (path in needUploadImages) {
            var flag = true
            var successCount = 0
            FileUploader().uploadPostImageToBBS(imageFile = File(path), listener = object : FileUploader.OnFileUploadListener {
                override fun onUploadFail(msg: String) {
                    flag = false
                }

                override fun onUploadSuccess(event: BaseEvent<UploadResultBean>) {

                    aidBuffer.append("${event.data.body?.attachment?.first()?.id},")
                    contentList.add(CONTENT_TYPE_IMG to event.data.body?.attachment?.first()?.urlName.toString())
                    if (++successCount == needUploadImages.size) {
                        aidBuffer.deleteCharAt(aidBuffer.lastIndex)
                        listener.onReplySend(toUid = toUid,
                                isQuote = isQuote,
                                replyId = replyId,
                                aid = aidBuffer.toString(),
                                contents = *contentList.toTypedArray())
                    }
                }
            })
            if (!flag) {
                HintUtils.show("上传图片失败")
                progress_bar_post_reply.visibility = View.GONE
                break
            }
        }
    }

    /**
     * 由主界面调用，传入必要的参数
     *
     * @param topicId 作者的id
     * @param toUid 回复给谁
     * @param replyId 此为引用回复时引用的回复的id
     * @param toUName 所回复用户的名称
     */
    fun showWithData(topicId: Int, toUid: Int, replyId: Int, toUName: String) {
        this.topicId = topicId
        this.toUid = toUid
        this.replyId = replyId
        this.toUName = toUName
        if (toUid == topicId) this.toUName += "(楼主)"
        text_view_post_reply_to_name.text = this.toUName
        if (lastToUid != toUid) {
            edit_view_post_reply.text.clear()
            needUploadImages.clear()
            imgGridViewAdapter?.notifyDataSetChanged()
            lastToUid = this.toUid
        }
        show()
    }

    /**
     * 显示底部框
     * 编辑视图获取焦点
     */
    override fun show() {
        super.show()
        edit_view_post_reply.requestFocus()
    }

    fun setImagePaths(imgPaths: List<String>) {
        needUploadImages.addAll(imgPaths)

        if (imgGridViewAdapter == null) {
            imgGridViewAdapter = ImgGridViewAdapter(context = activity, imgPaths = needUploadImages, forReply = true)
        }
        grid_view_post_reply_img.adapter = imgGridViewAdapter
        imgGridViewAdapter?.setImageClickListener(object : ImgGridViewAdapter.OnImageClickListener {
            override fun onImageClick(position: Int) {

            }

            override fun onImageDeleteClick(position: Int) {
                needUploadImages.removeAt(position)
                imgGridViewAdapter?.notifyDataSetChanged()
            }
        })
    }

    /**
     * 当回复成功发送之后，主界面将调用这个方法
     * 隐藏底部框和进度条
     */
    fun finish() {
        super.dismiss()
        progress_bar_post_reply.visibility = View.GONE
    }


}