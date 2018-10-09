package com.febers.uestc_bbs.module.more

import android.Manifest
import android.net.Uri
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.utils.FileUtils
import com.febers.uestc_bbs.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_image.*
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log.i
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target


class ImageActivity : BaseActivity() {

    private lateinit var permissionUtils: PermissionUtils
    private var url: String? = null
    private var imageUri: Uri? = null
    private var gifUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    private var gifDrawable: GifDrawable? = null
    private var gifBytes: ByteArray? = null

    override fun hideStatusBar(): Boolean = true

    override fun setView(): Int {
        url = intent.getStringExtra(IMAGE_URL)
        return R.layout.activity_image
    }

    /**
     * 加载URL对应的图片，支持两种格式
     * 由于ImageView不支持gif格式，因此采用Glide辅助加载的方式
     * 首先假设图片是一张gif，将其byte取出，保留下来
     * 如果没有拦截到异常，说明确实是一张gif图
     * 如果拦截到异常，说明不是一张gif图片，此时采用正常加载
     */
    override fun initView() {
        url ?: return
        i("Image", url)
        Thread(Runnable {
            try {
                i("Image", "gif")
                gifDrawable = Glide.with(this).load(url).asGif().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                gifBytes = gifDrawable?.data
                gifBytes ?: return@Runnable
                runOnUiThread {
                    Glide.with(this).load(url).into(image_view_image_activity)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                i("Image", "no gif")
                imageBitmap = Glide.with(this).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                runOnUiThread {
                    image_view_image_activity?.apply {
                        setImageBitmap(imageBitmap)
                        reset()
                        setOnClickListener {
                            finish()
                        }
                    }
                }
            }
        }).start()
        image_button_image_activity_save?.setOnClickListener { saveImage() }
        image_button_image_activity_share?.setOnClickListener { shareImage() }
    }

    private fun saveImage() {
        permissionUtils = PermissionUtils(this)
        permissionUtils.requestPermissions("请授予权限", object : PermissionUtils.PermissionListener {
            override fun doAfterGrand(vararg permission: String?) {
                Thread(Runnable {
                    if (imageBitmap != null) {
                        imageUri = FileUtils.saveImage(imageBitmap as Bitmap, forShare = false)
                        if (imageUri != null) {
                            showToast("保存图片成功")
                        } else {
                            showToast("保存图片失败")
                        }
                    } else if (gifBytes != null) {
                        gifUri = FileUtils.saveGif(gifBytes as ByteArray, forShare = false)
                    }
                    if (gifUri != null) {
                        showToast("保存图片成功")
                    } else {
                        showToast("保存图片成功")
                    }
                }).start()
            }
            override fun doAfterDenied(vararg permission: String?) {
                showToast("你拒绝了相应的权限")
            }
        }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun shareImage() {
        permissionUtils = PermissionUtils(this)
        permissionUtils.requestPermissions("请授予权限", object : PermissionUtils.PermissionListener {
            override fun doAfterGrand(vararg permission: String?) {
                Thread(Runnable {
                    if (gifBytes != null) {
                        if (gifUri == null) {
                            gifUri = FileUtils.saveGif(gifBytes as ByteArray, forShare = true)
                        }
                        if (gifUri == null) {
                            return@Runnable
                        }
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_STREAM, gifUri)
                        shareIntent.type = "image/*"
                        startActivity(shareIntent)
                    } else {
                        //第一次判断，是否已经保存过图片
                        if (imageUri == null) {
                            imageUri = FileUtils.saveImage(imageBitmap as Bitmap, forShare = true)
                        }
                        //再次判断，是否保存失败
                        if (imageUri == null) {
                            return@Runnable
                        }
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                        shareIntent.type = "image/*"
                        startActivity(shareIntent)
                    }
                }).start()}
            override fun doAfterDenied(vararg permission: String?) {
                showToast("你拒绝了相应的权限")
            }
        }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionUtils.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        FileUtils.viewDestroy()
    }
}
