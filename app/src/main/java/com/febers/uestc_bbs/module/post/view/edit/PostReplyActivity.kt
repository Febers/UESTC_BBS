package com.febers.uestc_bbs.module.post.view.edit

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.entity.UploadResultBean
import com.febers.uestc_bbs.io.FileUploader
import com.febers.uestc_bbs.module.post.contract.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.view.adapter.ImgGridViewAdapter
import com.febers.uestc_bbs.module.post.view.content.CONTENT_TYPE_IMG
import com.febers.uestc_bbs.module.post.view.content.CONTENT_TYPE_TEXT
import com.febers.uestc_bbs.lib.emotion.KeyBoardManager
import com.febers.uestc_bbs.lib.emotion.view.EmotionView
import kotlinx.android.synthetic.main.activity_post_reply.*
import com.febers.uestc_bbs.lib.emotion.EmotionTranslator
import com.febers.uestc_bbs.module.dialog.Dialog
import com.febers.uestc_bbs.utils.colorAccent
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.activity_post_reply.btn_edit_text_fullscreen
import kotlinx.android.synthetic.main.layout_quota_divide_line.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
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
    private lateinit var postPresenter: PostContract.Presenter
    private lateinit var keyboardManager: KeyBoardManager

    private val selectedImagePaths: MutableList<String> = ArrayList()
    private val selectedImageMedias: MutableList<LocalMedia> = ArrayList()

    private var toUserId: Int?  = 0
    private var toUserName: String? = null
    private var postId:Int? = 0
    private var replyId: Int? = 0
    private var isQuota: Int? = REPLY_NO_QUOTA
    private var description: String? = ""

    private var fullscreenEditView: EditViewFullscreen? = null
    private var progressDialog: AlertDialog? = null

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

    override fun setToolbar(): Toolbar? = toolbar_common

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
            beforeSendReply()
        }
        DrawableCompat.setTint(btn_post_reply_activity.drawable, colorAccent())
        initEmotionView()
        layout_divide_line.setBackgroundColor(colorAccent())
        btn_edit_text_fullscreen.setOnClickListener {
            if (fullscreenEditView == null) {
                fullscreenEditView = EditViewFullscreen(edit_view_post_reply_activity)
            }
            fullscreenEditView!!.show(supportFragmentManager, "fullscreen_edit")
        }

    }

    private fun initEmotionView() {
        (emotion_view_reply as EmotionView).attachEditText(edit_view_post_reply_activity)
        //TODO 当it为true时输入法弹出,当表情键盘弹出时将图标变成软键盘
        keyboardManager = KeyBoardManager.with(this@PostReplyActivity)
                .bindToEmotionButton(btn_emotion_post_reply)
                .setEmotionView(emotion_view_reply as EmotionView)
                .bindToLockContent(content_layout_post_reply)
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
        if (progressDialog == null) {
            progressDialog = Dialog.build(ctx) {
                progress("正在回复")
                cancelable(false)
            }
        }
        progressDialog!!.show()
        val aidBuffer = StringBuilder()
        val contentList: MutableList<Pair<Int, String>> = ArrayList()
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
        var successCount = 0
        for (path in selectedImagePaths.reversed()) {
            var flag = true
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

                override fun onUploading(msg: String) {
                }
            })
            if (!flag) {
                showError("上传第${successCount+1}张图片时失败,避免上传过大图片,请重试")
                progressDialog?.dismiss()
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
            progressDialog?.dismiss()
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
            progressDialog?.dismiss()
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