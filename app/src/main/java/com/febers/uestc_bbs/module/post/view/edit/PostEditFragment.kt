package com.febers.uestc_bbs.module.post.view.edit

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.entity.UploadResultBean
import com.febers.uestc_bbs.io.FileUploader
import com.febers.uestc_bbs.module.post.contract.PEditContract
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.presenter.PEditPresenterImpl
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.adapter.ImgGridViewAdapter
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_IMG
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_TEXT
import com.febers.uestc_bbs.view.emotion.KeyBoardManager
import com.febers.uestc_bbs.view.emotion.view.EmotionView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.fragment_post_edit.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.runOnUiThread
import java.io.File


class PostEditFragment: BaseFragment(), PEditContract.View, PListContract.View {

    private lateinit var pListPresenter: PListContract.Presenter
    private lateinit var pEditPresenter: PEditContract.Presenter

    private var isAnonymous: Int = USER_NO_ANONYMOUS //匿名，默认不匿名(0)
    private var isOnlyAuthor: Int = NO_ONLY_AUTHOR_VIEW_REPLY //回复仅作者可见，默认否(0)

    private lateinit var imgGridViewAdapter: ImgGridViewAdapter
    private val selectedImagePaths: MutableList<String> = ArrayList()
    private val selectedImageMedias: MutableList<LocalMedia> = ArrayList()

    private val boardNames: MutableList<String> = ArrayList()
    private val boardIds: MutableList<Int> = ArrayList()
    private var boardSpinnerAdapter: ArrayAdapter<String>? = null
    private var boardSpinner: Spinner? = null

    private val classificationNames: MutableList<String> = ArrayList()
    private val classificationIds: MutableList<Int> = ArrayList()
    private var classificationId: Int = 0
    private var classSpinnerAdapter: ArrayAdapter<String>? = null

    private var progressDialog: ProgressDialog? = null
    private lateinit var keyboardManager: KeyBoardManager

    override fun setView(): Int = R.layout.fragment_post_edit

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        pListPresenter = PListPresenterImpl(this)
        pEditPresenter = PEditPresenterImpl(this)

        initToolbar()
        boardNames.add(mTitle.toString())
        boardIds.add(mFid)
        boardSpinnerAdapter = ArrayAdapter(context!!,
                R.layout.item_layout_spinner,
                R.id.text_view_item_spinner,
                boardNames).apply {
            setDropDownViewResource(R.layout.item_layout_spinner_dropdown)
        }
        boardSpinner = Spinner(toolbar_post_edit.context).apply {
            adapter = boardSpinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mFid = boardIds[position]
                    classificationNames.clear()
                    classificationIds.clear()
                    classificationNames.add("不选择主题")
                    classificationIds.add(0)
                    classSpinnerAdapter?.notifyDataSetChanged()
                    pListPresenter.pListRequest(fid = mFid, page = 1, pageSize = 0)
                }
            }
        }
        toolbar_post_edit.addView(boardSpinner, 0)

        classificationNames.add("不选择主题")
        classificationIds.add(0)
        classSpinnerAdapter = ArrayAdapter(context!!,
                R.layout.item_layout_spinner,
                R.id.text_view_item_spinner,
                classificationNames).apply {
            setDropDownViewResource(R.layout.item_layout_spinner_dropdown)
        }
        spinner_post_edit_classification.apply {
            adapter = classSpinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    classificationId = classificationIds[position]
                }
            }
        }

        imgGridViewAdapter = ImgGridViewAdapter(context!!, selectedImagePaths)
        imgGridViewAdapter.setImageClickListener(object : ImgGridViewAdapter.OnImageClickListener {
            override fun onImageClick(position: Int) {
                PictureSelector.create(this@PostEditFragment)
                        .themeStyle(com.luck.picture.lib.R.style.picture_default_style)
                        .openExternalPreview(position, selectedImageMedias)
            }

            override fun onImageDeleteClick(position: Int) {
                selectedImagePaths.removeAt(position)
                selectedImageMedias.removeAt(position)
                imgGridViewAdapter.notifyDataSetChanged()
            }
        })
        grid_view_post_img.adapter = imgGridViewAdapter

        fab_post_edit.setOnClickListener { beforeSendNewPost() }
        btn_photo_post_edit.setOnClickListener { selectPictures() }
        initEmotionView()

        pListPresenter.boardListRequest(mFid)
        pListPresenter.pListRequest(fid = mFid, page = 1, pageSize = 0)

        //匿名功能无法实现
//        check_box_is_anonymous.setOnCheckedChangeListener { buttonView, isChecked ->
//            isAnonymous = if (isChecked) USER_ANONYMOUS else USER_NO_ANONYMOUS }
        check_box_is_anonymous.visibility = View.GONE
        check_box_is_only_author.setOnCheckedChangeListener { buttonView, isChecked ->
            isOnlyAuthor = if (isChecked) ONLY_AUTHOR_VIEW_REPLY else NO_ONLY_AUTHOR_VIEW_REPLY
        }
    }

    private fun initToolbar() {
        val activity: AppCompatActivity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar_post_edit)
        activity.supportActionBar?.apply {
            title = "编辑"
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar_post_edit.setNavigationOnClickListener { activity.finish() }
    }

    /**
     * 初始化表情键盘,需要注意的是当标题输入框获取焦点的时候
     * 表情键盘不应该出现,同时打开界面的时候，
     * 标题输入框应该第一个获得焦点
     */
    private fun initEmotionView() {
        (emotion_view_post_edit as EmotionView).attachEditText(edit_view_post_edit_content)
        keyboardManager = KeyBoardManager.with(activity)
                .bindToEmotionButton(btn_emotion_post_edit)
                .setEmotionView(emotion_view_post_edit as EmotionView)
                .bindToLockContent(content_view_post_edit)
                .setOnInputListener {
                    //当it为true时输入法弹出
                }
                .setOnEmotionButtonOnClickListener {
                    false
                }
        edit_view_post_title.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                keyboardManager.hideInputLayout()
                btn_emotion_post_edit.visibility = View.INVISIBLE
            } else {
                btn_emotion_post_edit.visibility = View.VISIBLE
            }
        }
        showSoftInput(edit_view_post_title)
    }

    /**
     * 发布新帖子
     */
    private fun beforeSendNewPost() {
        val stTitle = edit_view_post_title.text.toString()
        val stContent = edit_view_post_edit_content.text.toString()
        if (stTitle.isEmpty()) {
            edit_view_post_title.error = getString(R.string.please_input_title)
            return
        }
        if (stContent.isEmpty()) {
            edit_view_post_edit_content.error = getString(R.string.please_input_content)
            return
        }
        if (selectedImagePaths.size >= 9) {
            AlertDialog.Builder(context!!)
                    .setMessage("你要发布的帖子含有较多的图片，上传图片可能需要较长的时间")
                    .setTitle("提示")
                    .setPositiveButton("确认发布") { dialog, which -> sendNewPost(stTitle, stContent) }
                    .setNegativeButton("重新编辑") {dialog, which ->
                        return@setNegativeButton
                    }
                    .show()
        } else {
            sendNewPost(stTitle, stContent)
        }
    }

    private fun sendNewPost(stTitle: String, stContent: String) {
        if (progressDialog == null) {
            progressDialog = context!!.indeterminateProgressDialog("请稍候") {
                setCanceledOnTouchOutside(false)
            }
        }
        progressDialog?.show()
        if (selectedImagePaths.isEmpty()) {
            pEditPresenter.newPostRequest(fid = mFid, aid = "", typeId = 0,
                    title = stTitle,
                    anonymous = isAnonymous, onlyAuthor = isOnlyAuthor,
                    contents = *arrayOf(CONTENT_TYPE_TEXT to stContent))
        } else {
            sendImagePost(stTitle, stContent)
        }
    }

    private fun sendImagePost(title: String, content: String) {
        val aidBuffer = StringBuffer()
        val contentList: MutableList<Pair<Int, String>> = ArrayList()
        contentList.add(CONTENT_TYPE_TEXT to content)
        for (path in selectedImagePaths) {
            var flag = true
            var successCount = 0
            FileUploader().uploadPostImageToBBS(imageFile = File(path),
                    listener = object : FileUploader.OnFileUploadListener {
                        override fun onUploadFail(msg: String) {
                            flag = false
                        }

                        override fun onUploadSuccess(event: BaseEvent<UploadResultBean>) {
                            aidBuffer.append("${event.data.body?.attachment?.first()?.id},")
                            contentList.add(CONTENT_TYPE_IMG to event.data.body?.attachment?.first()?.urlName.toString())
                            if (++successCount == selectedImagePaths.size) {
                                aidBuffer.deleteCharAt(aidBuffer.lastIndex)
                                pEditPresenter.newPostRequest(fid = mFid,
                                        aid = aidBuffer.toString(),
                                        title = title,
                                        typeId = 0,
                                        anonymous = isAnonymous,
                                        onlyAuthor = isOnlyAuthor,
                                        contents = *contentList.toTypedArray())
                            }
                        }
                    })
            if (!flag) {
                showError("上传图片失败")
                break
            }
        }

    }

    @UiThread
    override fun showPList(event: BaseEvent<PostListBean>) {
        event.data.classificationType_list?.forEach {
            classificationNames.add(it.classificationType_name!!)
            classificationIds.add(it.classificationType_id)
        }
        classSpinnerAdapter?.notifyDataSetChanged()
    }

    @UiThread
    override fun showBoardList(event: BaseEvent<BoardListBean_>) {
        try {
            event.data.list?.get(0)?.board_list?.forEach {
                boardNames.add(it.board_name.toString())
                boardIds.add(it.board_id)
            }
            boardSpinnerAdapter?.notifyDataSetChanged()
        } catch (e: IndexOutOfBoundsException) {
        }
    }

    override fun showNewPostResult(event: PostSendResultBean) {
        context?.runOnUiThread {
            progressDialog?.dismiss()
            EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, PostNewEvent("success")))
            activity?.finish()
            PictureFileUtils.deleteCacheDirFile(context!!)
        }
    }

    private fun selectPictures() {
        hideSoftInput()
        PictureSelector.create(this@PostEditFragment)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(6)
                .isDragFrame(true)
                .enableCrop(true)
                .previewImage(true)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
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

    override fun showError(msg: String) {
        context?.runOnUiThread {
            showHint(msg)
            progressDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PictureFileUtils.deleteCacheDirFile(context!!)
        selectedImagePaths.clear()
        progressDialog = null
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: Int, title: String) =
                PostEditFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FID, fid)
                        putString(TITLE, title)
                    }
                }
    }
}