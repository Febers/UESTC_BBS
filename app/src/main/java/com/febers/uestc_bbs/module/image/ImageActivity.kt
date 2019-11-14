package com.febers.uestc_bbs.module.image

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.IMAGE_URLS
import com.febers.uestc_bbs.utils.log
import com.luck.picture.lib.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_image.*


class ImageActivity : BaseActivity() {

    private lateinit var imageUrls: Array<String>
    private lateinit var currentImageUrl: String
    private lateinit var positionArray: IntArray

    private var currentPosition: Int = 0

    override fun setView(): Int {
        imageUrls = intent.getStringArrayExtra(IMAGE_URLS)
        currentImageUrl = intent.getStringExtra(IMAGE_URL)
        positionArray = IntArray(imageUrls.size)
        return R.layout.activity_image
    }

    override fun enableHideStatusBar(): Boolean = true

    override fun initView() {
        if (!imageUrls.contains(currentImageUrl)) {
            log { "该url未被包含: $currentImageUrl" }
            finish()
        }
        setSwipeBackEnable(false)
        currentPosition = imageUrls.indexOf(currentImageUrl)
        positionArray.forEachIndexed { index, i -> positionArray[index] = -1 }    //初始化

        view_pager_image.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

            override fun getCount(): Int = imageUrls.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                if (imageUrls[position].isNotEmpty()) {
                    val view = PhotoView(mContext)
                    view.isEnabled = true
                    view.scaleType = ImageView.ScaleType.FIT_CENTER
                    view.setOnClickListener { finish() }
                    GlideApp.with(mContext)
                            .load(imageUrls[position])
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
            hideLoading()
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
    }

    private fun showLoading() {
        progress_bar_image.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar_image.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        view_pager_image.removeAllViews()
        super.onDestroy()
    }
}
