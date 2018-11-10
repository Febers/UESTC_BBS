package com.febers.uestc_bbs.module.post.view.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log.i
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.core.widget.NestedScrollView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.base.FID
import com.febers.uestc_bbs.base.TITLE
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.module.post.contract.PEditContract
import com.febers.uestc_bbs.module.post.presenter.PEditPresenterImpl
import com.febers.uestc_bbs.utils.FileUtils
import com.febers.uestc_bbs.view.adapter.ImgGridViewAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.fragment_post_edit.*

class PostEditFragment: BaseFragment(), PEditContract.View {

    private lateinit var pEditPresenter: PEditContract.Presenter
    private lateinit var imgGridViewAdapter: ImgGridViewAdapter
    private val imgPaths: MutableList<String> = ArrayList()
    private lateinit var addImagePath: String

    override fun setContentView(): Int {
        return R.layout.fragment_post_edit
    }

    override fun initView() {
        addImagePath = FileUtils.getResourceUri(context!!, R.drawable.ic_add_gray_small)
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
        btn_post_edit_img.setOnClickListener {
            selectPictures()
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

    private fun selectPictures() {
        PictureSelector.create(this@PostEditFragment)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(6)
                .enableCrop(true)
                .isDragFrame(true)
                .previewImage(true)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    private fun sendNewPost() {

    }

    override fun showNewPostResult(event: PostSendResultBean) {

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