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
    private lateinit var curImageUrl: String
    private lateinit var initialPositions: IntArray

    private var curPosition: Int = 0
    private var curPage: View? = null

    override fun setView(): Int {
        imageUrls = intent.getStringArrayExtra(IMAGE_URLS)
        curImageUrl = intent.getStringExtra(IMAGE_URL)
        initialPositions = IntArray(imageUrls.size)
        return R.layout.activity_image
    }

    override fun enableHideStatusBar(): Boolean = true

    override fun initView() {
        setSwipeBackEnable(false)
        curPosition = imageUrls.indexOf(curImageUrl)
        initialPositions.forEachIndexed { index, i -> initialPositions[index] = -1 }    //初始化

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
                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    initialPositions[position] = position
                                    return false
                                }
                            })
                            .into(view)
                    container.addView(view)
                    return view
                }
                return View(mContext) //是否有风险
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                initialPositions[position] = -1
                container.removeView(`object` as View)
            }

            override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
                curPage = `object` as View
            }
        }
        view_pager_image.currentItem = curPosition
        view_pager_image.tag = curPosition
        if (initialPositions[curPosition] != curPosition) {
            //未加载完毕
        }
        view_pager_image.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    override fun onDestroy() {
        curPage = null
        view_pager_image.removeAllViews()
        super.onDestroy()
    }
}
