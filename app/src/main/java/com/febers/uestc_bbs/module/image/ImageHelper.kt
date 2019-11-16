package com.febers.uestc_bbs.module.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.io.DownloadHelper
import com.febers.uestc_bbs.io.FileHelper.appImageDir2
import com.febers.uestc_bbs.io.tryClose
import com.febers.uestc_bbs.utils.logd
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch

object ImageHelper {

    private var fileForShare: Boolean = false
    private var imgFile: File? = null

    /**
     * 保存image
     *
     * @param bitmap
     */
    fun saveImage(context: Context, bitmap: Bitmap, forShare: Boolean): Uri? {
        fileForShare = forShare
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) { //内存未挂载
            return null
        }
        val end = if (forShare) ".jpg" else ""
        try {
            val path = "$appImageDir2${getImageFileName()}$end"
            imgFile = File(path)
            logd { """
                file? : ${imgFile!!.exists()}
                path : $path
                file path: ${imgFile!!.absolutePath}
            """.trimIndent() }
            val fos = FileOutputStream(imgFile!!)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.tryClose()

            insertImageToGallery(context, bitmap)
            return Uri.parse(imgFile!!.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun saveImage(context: Context, drawable: Drawable, forShare: Boolean): Uri? {
        return saveImage(context, drawable2Bitmap(drawable), forShare)
    }

    /**
     * 保存gif动图
     * 典型的头像链接为:http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=171264&size=big
     * 由于调用的DownloadHelper的download方法使用了回调接口，无法在回调方法直接返回，使用CountDownLatch
     * 使并发变成阻塞，相关知识Google
     *
     * @param context
     * @param gifUrl
     * @param forShare
     */
    fun saveGif(context: Context, gifUrl: String?, forShare: Boolean): Uri? {
        fileForShare = forShare
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) { //内存未挂载
            return null
        }
        if (gifUrl == null) {
            return null
        }
        val countDownLatch: CountDownLatch = CountDownLatch(1)
        try {
            DownloadHelper().download(url = gifUrl, fileName = getImageFileName() + ".gif", filePath = appImageDir2,
                    listener = object : DownloadHelper.OnDownloadListener {
                        override fun onDownloadSuccess(file: File) {
                            imgFile = file
                            countDownLatch.countDown()
                        }

                        override fun onDownloading(progress: Int) {
                        }

                        override fun onDownloadFailed() {
                            countDownLatch.countDown()
                        }
                    })
            countDownLatch.await()
//            insertImageToGallery(context, BitmapFactory.decodeFile(imgFile!!.absolutePath))
            insertGifToGallery(context)
            return Uri.parse(imgFile!!.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 给图片命名，方式简单粗暴，直接用时间字符串
     */
    private fun getImageFileName(): String {
        val now: Calendar = GregorianCalendar()
        val simpleDate: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return simpleDate.format(now.time)
    }

    /**
     * 保存图片之后插入到系统相册并且通知图库更新
     * 参考 三种方法，刷新 Android 的 MediaStore！让你保存的图片立即出现在相册里！ - 简书
     * https://www.jianshu.com/p/bc8b04bffddf
     *
     * @param context
     */
    private fun insertImageToGallery(context: Context, bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, imgFile!!.name, "test des")
    }

    /**
     * 保存gif，在Android10上这样的保存方式已失效，但是上一种通知相册刷新的方式只适用于图片不适用于动图(
     * 会直接在复制到Picture文件中，并且文件后面加上.jpg拓展)，尚未找到更合理的解决方法，故先保留
     */
    private fun insertGifToGallery(context: Context) {
        MediaScannerConnection.scanFile(context, arrayOf(imgFile!!.absolutePath), arrayOf("image/gif")) { path, uri ->
            logd { "save completed, path: $path"}
        }
    }

    /**
     * 在调用saveImage的函数的视图销毁之后，调用此方法
     * 删除图片文件
     *
     * 更新：使用新的刷新相册方式之后，会复制一份图片文件，所以需要两次删除
     */
    fun onImageViewDestroy() {
        imgFile ?: return
        if (fileForShare) { //分享，当前会被复制到/Picture，当前删除失效
            MyApp.context().contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA+"=?", arrayOf(imgFile!!.absolutePath))
        }
        if (imgFile!!.exists()) {   //不管是保存还是分享一定删除
            imgFile!!.delete()
        }
    }

    private fun drawable2Bitmap(drawable: Drawable): Bitmap {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        drawable.setBounds(0, 0, width, height)

        // 获取drawable的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565

        // 创建bitmap
        val bitmap = Bitmap.createBitmap(width, height, config)

        // 创建bitmap画布
        val canvas = Canvas(bitmap)

        // 将drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }
}