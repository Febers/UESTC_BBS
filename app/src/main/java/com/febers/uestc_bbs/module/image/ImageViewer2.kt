package com.febers.uestc_bbs.module.image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.Target
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.io.FileHelper
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.utils.log
import org.jetbrains.anko.runOnUiThread
import java.nio.ByteBuffer

class ImageViewer2: DialogFragment() {

    private var gifDrawable: GifDrawable? = null
    private var gifBytes: ByteArray? = null
    private var imageBitmap: Bitmap? = null
    private var imageUrl: String? = null
    private var imageUri: Uri? = null
    private var gifUri: Uri? = null

    private lateinit var imageView: ImageView
    private lateinit var btnShare: ImageButton
    private lateinit var btnSave: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

        imageUrl = arguments?.getString(IMAGE_URL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.image_viewer, container, false)

        imageView = view.findViewById(R.id.image_view_image_viewer)
        btnSave = view.findViewById(R.id.btn_image_viewer_save)
        btnShare = view.findViewById(R.id.btn_image_viewer_share)

        return view
    }

    //加载超大图时会崩溃
    override fun onStart() {
        super.onStart()
        try {
            Thread {
                if (!tryLoadImageAsGif()) {
                    if (!tryLoadImage()) {
                        context!!.runOnUiThread {
                            imageView.apply {
                                ImageLoader.loadResource(context, R.drawable.image_error_400200, this)
                                setOnClickListener {
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }.start()
        } catch (e: Exception) {
            log(e.toString())
        }
        btnSave.setOnClickListener { saveImage() }
        btnShare.setOnClickListener { shareImage() }
    }

    private fun tryLoadImageAsGif(): Boolean {
        try {
            gifDrawable = GlideApp.with(context!!)
                    .asGif()
                    .load(imageUrl)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get()

            //此时gifDrawable的类型为GifDrawable
            val gifBuffer: ByteBuffer? = gifDrawable?.buffer

            gifBuffer ?: return false
            gifBytes = FileHelper.getByteArrayFromByteBuffer(gifBuffer)

            context!!.runOnUiThread {
                GlideApp.with(this).asGif().load(imageUrl).into(imageView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    private fun tryLoadImage(): Boolean {
        try {
            val futureTarget = GlideApp.with(context!!).asBitmap().load(imageUrl)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            imageBitmap = futureTarget.get()
            context!!.runOnUiThread {
                imageView.apply {
                    setImageBitmap(imageBitmap)
                    setOnClickListener {
                        dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    private fun saveImage() {
        Thread {
            if (imageBitmap != null) {
                imageUri = ImageHelper.saveImage(context!!, imageBitmap as Bitmap, forShare = false)
                if (imageUri != null) {
                    HintUtils.show(activity, "图片已保存至 /storage/emulated/0/UESTC_BBS")
                } else {
                    HintUtils.show(activity, "保存图片失败")
                }
            } else if (gifBytes != null) {
                gifUri = ImageHelper.saveGif(context!!, imageUrl, false)
                if (gifUri != null) {
                    HintUtils.show(activity, "GIF 已保存至 /storage/emulated/0/UESTC_BBS")
                } else {
                    HintUtils.show(activity, "保存gif失败")
                }
            } else {
                HintUtils.show(activity, "保存失败")
            }
        }.start()
    }

    private fun shareImage() {
        Thread {
            if (gifBytes != null) {
                //第一次判断，是否已经保存过图片
                if (gifUri == null) {
                    gifUri = ImageHelper.saveGif(context!!, imageUrl, true)
                }
                //再次判断，是否保存失败
                if (gifUri == null) {
                    HintUtils.show(activity, "分享失败")
                    return@Thread
                }
                showShareView(gifUri as Uri)
            } else {
                if (imageUri == null) {
                    imageUri = ImageHelper.saveImage(context!!, imageBitmap as Bitmap, forShare = true)
                }
                if (imageUri == null) {
                    HintUtils.show(activity, "分享失败")
                    return@Thread
                }
                showShareView(imageUri as Uri)
            }
        }.start()
    }

    /**
     * 显示系统分享页面
     * 由于有新的Dialog加入，原来的dialog要dismiss
     * 否则会暂停在视图上
     *
     * @param uri gif或者img的URI
     */
    private fun showShareView(uri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        startActivity(shareIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        gifDrawable = null; gifBytes = null; gifUri = null
        imageBitmap = null; imageUri = null; imageUrl = null
        ImageHelper.onImageViewDestroy()
    }
}