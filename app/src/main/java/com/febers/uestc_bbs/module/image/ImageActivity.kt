package com.febers.uestc_bbs.module.image

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.IMAGE_URLS
import com.febers.uestc_bbs.utils.getWindowWidth
import com.febers.uestc_bbs.utils.loge
import com.luck.picture.lib.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_image.*


class ImageActivity : BaseActivity() {

    private val permissionRequestCode = 9527
    private val permissionRequestArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    private var imageUri: Uri? = null
    private var gifUri: Uri? = null

    private lateinit var imageUrls: Array<String>
    private lateinit var currentImageUrl: String
    private lateinit var positionArray: IntArray
    private lateinit var drawableArray: Array<Drawable?>

    private var currentPosition: Int = 0

    override fun setView(): Int {
        imageUrls = intent.getStringArrayExtra(IMAGE_URLS)
        currentImageUrl = intent.getStringExtra(IMAGE_URL)
        positionArray = IntArray(imageUrls.size)
        drawableArray = Array(size = imageUrls.size, init = {null})
        return R.layout.activity_image
    }

    override fun enableHideStatusBar(): Boolean = true

    override fun initView() {
        if (!imageUrls.contains(currentImageUrl)) {
            loge { "该url未被包含: $currentImageUrl" }
            finish()
        }
        setSwipeBackEnable(false)
        currentPosition = imageUrls.indexOf(currentImageUrl)
        positionArray.forEachIndexed { index, i -> positionArray[index] = -1 }    //初始化
        drawableArray.forEachIndexed { index, drawable -> drawableArray[index] = null }

        view_pager_image.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

            override fun getCount(): Int = imageUrls.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                if (imageUrls[position].isNotEmpty()) {
                    val view = PhotoView(mContext)
                    view.isEnabled = true
                    view.scaleType = ImageView.ScaleType.FIT_CENTER
                    view.setOnClickListener {
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    GlideApp.with(mContext)
                            .load(imageUrls[position])
                            .override(getWindowWidth(), getWindowWidth())
                            .fitCenter()
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                    if (currentPosition == position) {
                                        hideLoading()
                                    }
                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    positionArray[position] = position
                                    drawableArray[position] = resource
                                    if (currentPosition == position) {
                                        hideLoading()
                                    }
                                    return false
                                }
                            })
                            .into(view)
                    container.addView(view)
                    return view
                }
                return PhotoView(mContext) //是否有风险
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                positionArray[position] = -1
                container.removeView(`object` as View)
            }

            override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            }
        }

        view_pager_image.currentItem = currentPosition
        tv_image_order.text = "${currentPosition+1}/${imageUrls.size}"
        if (positionArray[currentPosition] != currentPosition) {
            //未加载完毕
            showLoading()
        }
        view_pager_image.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }
            override fun onPageSelected(position: Int) {
                if (positionArray[position] != position) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
                    showLoading()
                } else {
                    hideLoading()
                }
                currentPosition = position
                tv_image_order.text = "${currentPosition+1}/${imageUrls.size}"
            }
        })

        btn_image_viewer_save.setOnClickListener { saveImage() }
        btn_image_viewer_share.setOnClickListener { shareImage() }
    }

    private fun showLoading() {
        progress_bar_image.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar_image.visibility = View.INVISIBLE
    }

    private fun saveImage() {
        val p = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (p != PackageManager.PERMISSION_GRANTED) {
            loge { "出错：应用没有读写手机存储的权限！" }
            showHint("出错：应用没有读写手机存储的权限！")
            ActivityCompat.requestPermissions(mContext, permissionRequestArray, permissionRequestCode)
            return
        }
        val drawable = drawableArray[currentPosition] ?: return
        Thread {
            if (drawable is GifDrawable) {
                gifUri = ImageHelper.saveGif(mContext, imageUrls[currentPosition], false)
                if (gifUri == null) showHint("gif保存失败") else showHint("gif保存成功")
            } else {
                imageUri = ImageHelper.saveImage(mContext, drawable, false)
                if (imageUri == null) showHint("图片保存失败") else showHint("图片已保存至相册")
            }
        }.start()
    }

    private fun shareImage() {
        val drawable = drawableArray[currentPosition]
        if (drawable == null) {
            showHint("图片未加载完毕，请稍候")
            return
        }
        Thread {
            if (drawable is GifDrawable) {
                //第一次判断，是否已经保存过图片
                if (gifUri == null) {
                    gifUri = ImageHelper.saveGif(mContext, imageUrls[currentPosition], true)
                }
                //再次判断，是否保存失败
                if (gifUri == null) {
                    showHint("分享失败")
                    return@Thread
                }
                showShareView(gifUri as Uri)
            } else {
                if (imageUri == null) {
                    imageUri = ImageHelper.saveImage(mContext, drawable, forShare = true)
                }
                if (imageUri == null) {
                    showHint("分享失败")
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
        view_pager_image.removeAllViews()
        ImageHelper.onImageViewDestroy()
        super.onDestroy()
    }
}
