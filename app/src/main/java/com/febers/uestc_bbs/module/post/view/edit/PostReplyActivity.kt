package com.febers.uestc_bbs.module.post.view.edit

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.entity.UploadResultBean
import com.febers.uestc_bbs.io.FileUploader
import com.febers.uestc_bbs.module.post.contract.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.view.adapter.ImgGridViewAdapter
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_IMG
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_TEXT
import com.febers.uestc_bbs.view.emotion.KeyBoardManager
import com.febers.uestc_bbs.view.emotion.view.EmotionView
import kotlinx.android.synthetic.main.activity_post_reply.*
import com.febers.uestc_bbs.view.emotion.EmotionTranslator
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.PictureFileUtils
import java.io.File
import java.lang.StringBuilder
import java.util.ArrayList

const val POST_ID = "post_id"
const val POST_REPLY_ID = "post_reply_id"
const val POST_REPLY_IS_QUOTA = "post_reply_is_quota"
const val POST_REPLY_DESCRIPTION = "post_reply_description"

const val POST_REPLY_RESULT_CODE = 418
const val POST_REPLY_RESULT = "post_reply_result"

class PostReplyActivity: BaseActivity(), PostContract.View {

    private lateinit var imgGridViewAdapter: ImgGridViewAdapter
    private val selectedImagePaths: MutableList<String> = ArrayList()
    private val selectedImageMedias: MutableList<LocalMedia> = ArrayList()
    private lateinit var postPresenter: PostContract.Presenter

    private var toUserId: Int?  = 0
    private var toUserName: String? = null
    private var postId:Int? = 0
    private var replyId: Int? = 0
    private var isQuota: Int? = REPLY_NO_QUOTA
    private var description: String? = ""

    private lateinit var keyboardManager: KeyBoardManager

    override fun setView(): Int {
        intent?.let {
            toUserId = it.getIntExtra(USER_ID, 0)
            toUserName = it.getStringExtra(USER_NAME)
            postId = it.getIntExtra(POST_ID, 0)
            replyId = it.getIntExtra(POST_REPLY_ID, 0)
            isQuota = if (it.getBooleanExtra(POST_REPLY_IS_QUOTA, false)) REPLY_QUOTA
                else REPLY_NO_QUOTA
            description = it.getStringExtra(POST_REPLY_DESCRIPTION)
        }
        return R.layout.activity_post_reply
    }

    override fun setToolbar(): Toolbar? = toolbar_post_reply

    override fun initView() {
        postPresenter = PostPresenterImpl(this@PostReplyActivity)

        supportActionBar?.title = "回复 $toUserName"
        text_view_reply_quota_activity.text = description

        imgGridViewAdapter = ImgGridViewAdapter(this@PostReplyActivity, selectedImagePaths, true)
        imgGridViewAdapter.setImageClickListener(object : ImgGridViewAdapter.OnImageClickListener {
            override fun onImageClick(position: Int) {
                PictureSelector.create(this@PostReplyActivity)
                        .themeStyle(com.luck.picture.lib.R.style.picture_default_style)
                        .openExternalPreview(position, selectedImageMedias)

            }

            override fun onImageDeleteClick(position: Int) {
                selectedImagePaths.removeAt(position)
                selectedImageMedias.removeAt(position)
                imgGridViewAdapter.notifyDataSetChanged()
            }
        })
        grid_view_post_reply_img.adapter = imgGridViewAdapter

        btn_photo_post_reply.setOnClickListener { selectPictures() }
        btn_post_reply_activity.setOnClickListener {
            if (progress_bar_reply_activity.visibility != View.VISIBLE) {
                beforeSendReply()
            }
        }
        initEmotionView()
    }

    private fun initEmotionView() {
        (emotion_view_reply as EmotionView).attachEditText(edit_view_post_reply_activity)
        //TODO 当it为true时输入法弹出,当表情键盘弹出时将图标变成软键盘
        keyboardManager = KeyBoardManager.with(this@PostReplyActivity)
                .bindToEmotionButton(btn_emotion_post_reply)
                .setEmotionView(emotion_view_reply as EmotionView)
                .bindToLockContent(content_layout_post_reply)
//                .setOnInputListener {
//
//                }
//                .setOnEmotionButtonOnClickListener {
//                    false
//                }
    }

    private fun beforeSendReply() {
        val stContent: String = edit_view_post_reply_activity.text.toString()
        if (stContent.isEmpty()) {
            if (selectedImagePaths.isEmpty()) {
                edit_view_post_reply_activity.error = "内容不能为空"
                return
            }
        }
        if (selectedImagePaths.size >= 9) {
            AlertDialog.Builder(this@PostReplyActivity)
                    .setMessage("你要回复的内容含有较多图片，上传图片可能需要较长的时间")
                    .setTitle("提示")
                    .setPositiveButton("确认发布") { dialog, which -> sendReply(stContent) }
                    .setNegativeButton("重新编辑") {dialog, which ->
                        return@setNegativeButton
                    }
                    .show()
        } else {
            sendReply(stContent)
        }
    }

    /**
     * 发送评论，注意的是回复有包含图片和不包含图片的类型
     *
     */
    private fun sendReply(stContent: String) {
        progress_bar_reply_activity.visibility = View.VISIBLE

        val aidBuffer = StringBuilder()
        val contentList: MutableList<Pair<Int, String>> = java.util.ArrayList()
        contentList.add(CONTENT_TYPE_TEXT to stContent)
        if (selectedImagePaths.isEmpty()) {
            postPresenter.postReplyRequest(
                    postId = postId!!,
                    isQuota = isQuota!!,
                    replyId = replyId!!,
                    aid = aidBuffer.toString(),
                    contents = *contentList.toTypedArray())
            return
        }
        for (path in selectedImagePaths) {
            var flag = true
            var successCount = 0
            FileUploader().uploadPostImageToBBS(imageFile = File(path), listener = object : FileUploader.OnFileUploadListener {
                override fun onUploadFail(msg: String) {
                    flag = false
                }

                override fun onUploadSuccess(event: BaseEvent<UploadResultBean>) {

                    aidBuffer.append("${event.data.body?.attachment?.first()?.id},")
                    contentList.add(CONTENT_TYPE_IMG to event.data.body?.attachment?.first()?.urlName.toString())
                    if (++successCount == selectedImagePaths.size) {
                        aidBuffer.deleteCharAt(aidBuffer.lastIndex)
                        postPresenter.postReplyRequest(
                                postId = postId!!,
                                isQuota = isQuota!!,
                                replyId = replyId!!,
                                aid = aidBuffer.toString(),
                                contents = *contentList.toTypedArray())
                    }
                }
            })
            if (!flag) {
                showError("上传图片失败")
                progress_bar_reply_activity.visibility = View.GONE
                break
            }
        }
    }

    /**
     * 回复的结果，只有回复成功的时候
     * 才给帖子详情界面发送成功的信息
     *
     */
    override fun showPostReplyResult(event: BaseEvent<PostSendResultBean>) {
        runOnUiThread{
            progress_bar_reply_activity.visibility = View.GONE
            showHint(event.data.head?.errInfo.toString())
            setResult(POST_REPLY_RESULT_CODE, Intent().apply {
                    putExtra(POST_REPLY_RESULT, true)
                })
            finish()
        }
    }

    private fun selectPictures() {
        PictureSelector.create(this@PostReplyActivity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .isDragFrame(true)
                .enableCrop(true)
                .previewImage(false)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun showError(msg: String) {
        runOnUiThread{
            showHint(msg)
            progress_bar_reply_activity?.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            val tempImages = PictureSelector.obtainMultipleResult(data)
            selectedImageMedias.addAll(tempImages)
            tempImages.forEach {
                selectedImagePaths.add(selectedImagePaths.size, it.cutPath)
            }
            imgGridViewAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 如果表情键盘正在显示，拦截返回键事件
     * 否则将处理逻辑交给上层
     *
     * 将emotionViewVisible的状态更新
     */
    override fun onBackPressed() {
        if (!MyApp.emotionViewVisible) {
            super.onBackPressed()
        }
        MyApp.emotionViewVisible = false
    }

    override fun onResume() {
        super.onResume()
        EmotionTranslator.getInstance().resumeGif(localClassName)
    }

    override fun onPause() {
        super.onPause()
        EmotionTranslator.getInstance().pauseGif()
    }

    override fun onDestroy() {
        super.onDestroy()
        PictureFileUtils.deleteCacheDirFile(this@PostReplyActivity)
        EmotionTranslator.getInstance().clearGif(localClassName)
    }
}