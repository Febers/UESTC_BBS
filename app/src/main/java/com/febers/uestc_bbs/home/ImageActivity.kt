package com.febers.uestc_bbs.home

import android.Manifest
import android.net.Uri
import android.provider.MediaStore
import android.support.annotation.UiThread
import android.support.design.widget.Snackbar
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.utils.FileUtils
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log.i


class ImageActivity : BaseActivity() {

    private lateinit var permissionHelper: PermissionHelper
    private var url: String? = null
    private var imageUri: Uri? = null

    override fun noStatusBar(): Boolean = true

    override fun setView(): Int {
        url = intent.getStringExtra(IMAGE_URL)
        return R.layout.activity_image
    }

    override fun initView() {
        url ?: return
        ImageLoader.load(this, url, image_view_image_activity, placeImage = R.mipmap.ic_place_holder_grey_square, isCircle = false, isBlur = false, noCache = true)
        image_view_image_activity?.apply {
            setOnClickListener {
            }
        }
        image_button_image_activity_save?.setOnClickListener {
            if(image_view_image_activity.drawable is GifDrawable) {
                showSnackBar("暂不支持保存动态图")
            } else {
                saveImage()
            }
        }
        image_button_image_activity_share?.setOnClickListener {
            if(image_view_image_activity.drawable is GifDrawable) {
                showSnackBar("暂不支持分享动态图")
            } else {
                shareImage()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun saveImage() {
        permissionHelper = PermissionHelper(this)
        permissionHelper.requestPermissions("请授予权限", object : PermissionHelper.PermissionListener {
            override fun doAfterGrand(vararg permission: String?) {
                image_view_image_activity.isDrawingCacheEnabled = true
                val bitmap = image_view_image_activity.drawingCache
                Thread(Runnable {
                    imageUri = FileUtils.saveImage(bitmap)
                    if (imageUri != null) {
                        showSnackBar("保存图片成功")
                    } else {
                        showSnackBar("保存图片失败")
                    }
                    image_view_image_activity.isDrawingCacheEnabled = false
                }).start()
            }
            override fun doAfterDenied(vararg permission: String?) {
                toast("你拒绝了相应的权限")
            }
        }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun shareImage() {
        Thread(Runnable {
            if (imageUri == null) {
                image_view_image_activity.isDrawingCacheEnabled = true
                val bitmap = image_view_image_activity.drawingCache
                imageUri = FileUtils.saveImage(bitmap)
                if (imageUri == null) {
                    showSnackBar("分享失败")
                    return@Runnable
                } else {
                    showSnackBar("分享成功")
                }
                image_view_image_activity.isDrawingCacheEnabled = false
            }
            //sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.type = "image/*"
            startActivity(shareIntent)
        }).start()
    }

    @UiThread
    private fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(this.window.decorView, msg, Snackbar.LENGTH_SHORT)
        snackBar.show()
    }
}
