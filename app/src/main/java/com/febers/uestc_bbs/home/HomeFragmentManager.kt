package com.febers.uestc_bbs.home

import android.support.v4.app.Fragment
import com.febers.uestc_bbs.post.PostsFragment

/**
 * Created by Febers on img_2018/2/3.
 */

object HomeFragmentManager {

    private var sPostsFragment: PostsFragment? = null

    fun getInstance(position: Int): Fragment {
        when (position) {
            0 -> {
                if (sPostsFragment == null) {
                    sPostsFragment = PostsFragment()
                }
                return sPostsFragment as Fragment
            }
        }
        return Fragment()
    }
}
