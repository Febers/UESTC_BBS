package com.febers.uestc_bbs.module.image

import android.app.ProgressDialog
import android.net.Uri
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.IMAGE_URL
import kotlinx.android.synthetic.main.activity_image.*
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log.i
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.febers.uestc_bbs.base.BaseActivity
import org.jetbrains.anko.indeterminateProgressDialog


class AppImgViewer : BaseActivity() {

    private lateinit var progressDialog: ProgressDialog
    private var gifDrawable: GifDrawable? = null
    private var gifBytes: ByteArray? = null
    private var imageBitmap: Bitmap? = null
    private var imageUrl: String? = null
    private var imageUri: Uri? = null
    private var gifUri: Uri? = null

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int {
        imageUrl = intent.getStringExtra(IMAGE_URL)
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
        imageUrl ?: return
//        i("Image imageUrl:", imageUrl)
        progressDialog = this.indeterminateProgressDialog("请稍候") {
            setCanceledOnTouchOutside(false)
            show()
        }
        Thread(Runnable {
            if (!tryLoadImageAsGif()) {
                if (!tryLoadImage()) {
                    runOnUiThread {
                        image_view_image_activity?.apply {
                            Glide.with(context).load(R.mipmap.image_error_400200).into(this)
                            setOnClickListener {
                                finish()
                                overridePendingTransition(0, 0)
                            }
                        }
                        progressDialog.hide()
                    }
                }
            }
        }).start()
        image_button_image_activity_save?.setOnClickListener { saveImage() }
        image_button_image_activity_share?.setOnClickListener { shareImage() }
    }

    private fun tryLoadImageAsGif(): Boolean {
        try {
            gifDrawable = Glide.with(this).asGif().load(imageUrl).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            gifBytes = gifDrawable?.buffer?.array()
            gifBytes ?: return false
            runOnUiThread {
//                i("Image", "gif")
                Glide.with(this).load(imageUrl).into(image_view_image_activity)
                progressDialog.hide()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    private fun tryLoadImage(): Boolean {
        try {
            val futureTarget = Glide.with(this).asBitmap().load(imageUrl).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            imageBitmap = futureTarget.get()
            runOnUiThread {
                image_view_image_activity?.apply {
                    i("Image", "img")
                    setImageBitmap(imageBitmap)
                    reset()
                    setOnClickListener {
                        finish()
                    }

                }
                progressDialog.hide()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    private fun saveImage() {
        progressDialog.show()
        Thread(Runnable {
            if (imageBitmap != null) {
                imageUri = ImageHelper.saveImage(imageBitmap as Bitmap, forShare = false)
                if (imageUri != null) {
                    showToast("保存图片成功")
                } else {
                    showToast("保存图片失败")
                }
            } else if (gifBytes != null) {
                gifUri = ImageHelper.saveGif(gifBytes as ByteArray, forShare = false)
                if (gifUri != null) {
                    showToast("保存图片成功")
                } else {
                    showToast("保存图片成功")
                }
            }
            runOnUiThread { progressDialog.hide() }
        }).start()
    }

    private fun shareImage() {
        progressDialog.show()
        Thread(Runnable {
            if (gifBytes != null) {
                //第一次判断，是否已经保存过图片
                if (gifUri == null) {
                    gifUri = ImageHelper.saveGif(gifBytes as ByteArray, forShare = true)
                }
                //再次判断，是否保存失败
                if (gifUri == null) {
                    return@Runnable
                }
                showShareView(gifUri as Uri)
            } else {
                if (imageUri == null) {
                    imageUri = ImageHelper.saveImage(imageBitmap as Bitmap, forShare = true)
                }
                if (imageUri == null) {
                    return@Runnable
                }
                showShareView(imageUri as Uri)
            }
        }).start()
    }


    /**
     * 显示系统分享页面
     * 由于有新的Dialog加入，原来的dialog要dismiss
     * 否则会暂停在视图上
     * @param uri gif或者img的URI
     */
    private fun showShareView(uri: Uri) {
        runOnUiThread { progressDialog.dismiss() }
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        startActivity(shareIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageHelper.onImageViewDestroy()
    }
}
