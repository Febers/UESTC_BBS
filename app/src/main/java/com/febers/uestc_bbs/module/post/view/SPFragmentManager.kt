/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

/*
 * Created by Febers on 18-6-9 上午9:21.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-4 下午9:48.
 */

import android.os.Bundle
import android.support.v4.app.Fragment
import com.febers.uestc_bbs.module.post.view.SubPostFragment

object SPFragmentManager {

    private val bundle: Bundle = Bundle()
    private var subPostFragment0: SubPostFragment? = null
    private var subPostFragment1: SubPostFragment? = null
    private var subPostFragment2: SubPostFragment? = null

    fun getNewPostFragment(): Fragment{
        if (subPostFragment0 == null) {
            bundle.putInt("position", 0)
            subPostFragment0 = SubPostFragment.getInstance(bundle)
        }
        return subPostFragment0!!
    }

    fun getNewReplayFragment(): Fragment{
        if (subPostFragment1 == null) {
            bundle.putInt("position", 1)
            subPostFragment1 = SubPostFragment.getInstance(bundle)
        }
        return subPostFragment1!!
    }

    fun getHotArticleFragment(): Fragment{
        if (subPostFragment2 == null) {
            bundle.putInt("position", 2)
            subPostFragment2 = SubPostFragment.getInstance(bundle)
        }
        return subPostFragment2!!
    }
}