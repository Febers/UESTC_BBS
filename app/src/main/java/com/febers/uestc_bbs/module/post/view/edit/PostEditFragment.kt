package com.febers.uestc_bbs.module.post.view.edit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.base.FID
import kotlinx.android.synthetic.main.fragment_post_edit.*

class PostEditFragment: BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_post_edit
    }

    override fun initView() {
        initToolbar()
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

    private fun initToolbar() {
        val activity: AppCompatActivity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar_post_edit)
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar_post_edit.setNavigationOnClickListener { activity.finish() }
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: Int) =
                PostEditFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FID, fid)
                    }
                }
    }
}