package com.febers.uestc_bbs.module.post.view.edit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.entity.UploadResultBean
import com.febers.uestc_bbs.io.FileHelper
import com.febers.uestc_bbs.io.FileUploader
import com.febers.uestc_bbs.module.post.contract.PEditContract
import com.febers.uestc_bbs.module.post.presenter.PEditPresenterImpl
import com.febers.uestc_bbs.utils.LogUtils
import com.febers.uestc_bbs.utils.ToastUtils
import com.febers.uestc_bbs.view.adapter.ImgGridViewAdapter
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_IMG
import com.febers.uestc_bbs.view.helper.CONTENT_TYPE_TEXT
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.fragment_post_edit.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.runOnUiThread
import java.io.File
import java.lang.StringBuilder
import java.util.*

class PostEditFragment: BaseFragment(), PEditContract.View {

    private lateinit var pEditPresenter: PEditContract.Presenter
    private lateinit var imgGridViewAdapter: ImgGridViewAdapter
    private val imgPaths: MutableList<String> = ArrayList()

    private var progressDialog: ProgressDialog? = null

    private lateinit var addImagePath: String

    override fun setContentView(): Int {
        return R.layout.fragment_post_edit
    }

    override fun initView() {
        addImagePath = FileHelper.getUriByResourceId(context!!, R.drawable.ic_add_gray_small)
        imgPaths.add(addImagePath)
        pEditPresenter = PEditPresenterImpl(this)
        initToolbar(); initEmotionButton(); onScrollViewChange()

        imgGridViewAdapter = ImgGridViewAdapter(context!!, imgPaths)
        imgGridViewAdapter.setImageClickListener(object : ImgGridViewAdapter.OnImageClickListener {
            override fun onImageClick(position: Int) {
                if (position + 1 == imgPaths.size) {
                    selectPictures()
                }
            }

            override fun onImageDeleteClick(position: Int) {
                imgPaths.removeAt(position)
                imgGridViewAdapter.notifyDataSetChanged()
            }
        })
        grid_view_post_img.adapter = imgGridViewAdapter

        fab_post_edit.setOnClickListener {
            sendNewPost()
        }
    }

    /**
     * 发布新帖子
     */
    private fun sendNewPost() {
        val titleString = edit_view_post_title.text.toString()
        val contentString = edit_view_post_content.text.toString()
        if (titleString.isEmpty()) edit_view_post_title.error = "请输入标题"
        if (contentString.isEmpty()) edit_view_post_content.error = "请输入内容"
        if (progressDialog == null) {
            progressDialog = context!!.indeterminateProgressDialog("请稍候") {
                setCanceledOnTouchOutside(false)
            }
        }
        progressDialog?.show()
        if (imgPaths.size == 1) {
            pEditPresenter.newPostRequest(fid = mFid, aid = "", title = titleString, contents = *arrayOf(CONTENT_TYPE_TEXT to contentString))
        } else {
            sendImagePost(titleString, contentString)
        }
    }

    private fun sendImagePost(title: String, content: String) {
        val needUploadImages: MutableList<String> = ArrayList()
        needUploadImages.addAll(imgPaths)
        needUploadImages.remove(addImagePath)

        val aidBuffer = StringBuffer()
        val contentList: MutableList<Pair<Int, String>> = Collections.synchronizedList(ArrayList())
        contentList.add(CONTENT_TYPE_TEXT to content)
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
                    LogUtils.i("success")
                    if (++successCount == needUploadImages.size) {
                        aidBuffer.deleteCharAt(aidBuffer.lastIndex)
                        LogUtils.i("new size: ${contentList.size}")
                        pEditPresenter.newPostRequest(fid = mFid, aid = aidBuffer.toString(), title = title, contents = *contentList.toTypedArray())
                    }
                }
            })
            if (!flag) {
                ToastUtils.show("失败")
                break
            }
        }

    }

    override fun showNewPostResult(event: PostSendResultBean) {
        context?.runOnUiThread {
            progressDialog?.dismiss()
            EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, "new post"))
            activity?.finish()
            PictureFileUtils.deleteCacheDirFile(context!!)
        }
    }

    private fun selectPictures() {
        PictureSelector.create(this@PostEditFragment)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(6)
                .enableCrop(true)
                .isDragFrame(true)
                .previewImage(true)
                .compress(true)
                .hideBottomControls(false)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            //删除添加图片的占位图片
            imgPaths.removeAt(imgPaths.size-1)
            selectList.forEach {
                imgPaths.add(imgPaths.size, it.cutPath)
            }
            //将添加图片的占位图片重新添加
            imgPaths.add(addImagePath)
            imgGridViewAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 不能直接监听输入法的显示与否
     * 通过监听ScrollView的变化，判断其是否显示
     * 然后进行相应的设置，比如表情界面的显示
     */
    private fun onScrollViewChange() {
        scroll_view_post_edit.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int,
                                                          scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val diff = oldScrollY - scrollY
            if (diff > 700) {
                frame_layout_emotion.visibility = View.GONE
            }
        }
    }

    //初始化表情按钮
    private fun initEmotionButton() {
        val btnEmotionClickListener: View.OnClickListener = View.OnClickListener {
            if (frame_layout_emotion.visibility == View.GONE) {
                hideSoftInput()
                frame_layout_emotion.visibility = View.VISIBLE
            } else {
                frame_layout_emotion.visibility = View.GONE
            }
        }
        btn_post_edit_emotion.setOnClickListener(btnEmotionClickListener)
    }

    //初始化标题
    private fun initToolbar() {
        val activity: AppCompatActivity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar_post_edit)
        activity.supportActionBar?.apply {
            title = mTitle
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar_post_edit.setNavigationOnClickListener { activity.finish() }
    }

    override fun showError(msg: String) {
        context?.runOnUiThread {
            showToast(msg)
            progressDialog?.dismiss()
            PictureFileUtils.deleteCacheDirFile(context!!)
        }
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