package com.febers.uestc_bbs.home

import android.Manifest
import android.net.Uri
import android.support.annotation.UiThread
import android.support.design.widget.Snackbar
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.utils.FileUtils
import com.febers.uestc_bbs.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import java.io.File


class ImageActivity : BaseActivity() {

    private lateinit var permissionHelper: PermissionHelper
    private var url: String? = null
    private var imageUri: Uri? = null
    private var gifUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    private var gifDrawable: GifDrawable? = null
    private var gifBytes: ByteArray? = null

    override fun noStatusBar(): Boolean = true

    override fun setView(): Int {
        url = intent.getStringExtra(IMAGE_URL)
        return R.layout.activity_image
    }

    override fun initView() {
        url ?: return
        Thread(Runnable {
            if (url.toString().endsWith("gif")) {
                gifDrawable = Glide.with(this).load(url).asGif().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                gifBytes = gifDrawable?.data
                runOnUiThread {
                    gifBytes ?: return@runOnUiThread
                    image_view_image_activity.setImageDrawable(gifDrawable)
                }
            } else {
                imageBitmap = Glide.with(this).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                runOnUiThread {
                    image_view_image_activity.setImageBitmap(imageBitmap)
                }
            }
        }).start()

        image_view_image_activity?.apply {
            setOnClickListener {
            }
        }
        image_button_image_activity_save?.setOnClickListener {
            saveImage()
        }
        image_button_image_activity_share?.setOnClickListener {
            shareImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun saveImage() {
        permissionHelper = PermissionHelper(this)
        permissionHelper.requestPermissions("请授予权限", object : PermissionHelper.PermissionListener {
            override fun doAfterGrand(vararg permission: String?) {
                Thread(Runnable {
                    if (imageBitmap != null) {
                        imageUri = FileUtils.saveImage(imageBitmap as Bitmap)
                        if (imageUri != null) {
                            showSnackBar("保存图片成功")
                        } else {
                            showSnackBar("保存图片失败")
                        }
                    } else if (gifBytes != null) {
                        gifUri = FileUtils.saveGif(gifBytes as ByteArray)
                    }
                    if (gifUri != null) {
                        showSnackBar("保存图片成功")
                    } else {
                        showSnackBar("保存图片成功")
                    }
                }).start()
            }
            override fun doAfterDenied(vararg permission: String?) {
                toast("你拒绝了相应的权限")
            }
        }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun shareImage() {
        imageBitmap ?: return
        Thread(Runnable {
            //第一次判断，是否已经保存过图片
            if (imageUri == null) {
                imageUri = FileUtils.saveImage(imageBitmap as Bitmap)
            }
            //再次判断，是否保存失败
            if (imageUri == null) {
                showSnackBar("分享失败")
                return@Runnable
            } else {
                showSnackBar("分享成功")
            }
            //通过广播通知系统扫描图片，以便在相册中查看
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
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
