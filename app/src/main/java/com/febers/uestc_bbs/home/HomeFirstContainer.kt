package com.febers.uestc_bbs.home

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.module.post.view.PListPagerFragment

class HomeFirstContainer : BaseFragment() {

    override fun setView(): Int = R.layout.container_home_first

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (findChildFragment(PListPagerFragment::class.java) == null) {
            loadRootFragment(R.id.home_first_container, PListPagerFragment())
        }
    }
}

/**
* 程序出Bug了？
* 　　　∩∩
* 　　（´･ω･）
* 　 ＿|　⊃／(＿＿_
* 　／ └-(＿＿＿／
* 　￣￣￣￣￣￣￣
* 算了反正不是我写的
* 　　 ⊂⌒／ヽ-、＿
* 　／⊂_/＿＿＿＿ ／
* 　￣￣￣￣￣￣￣
* 不对，好像都是我写的
* 　　　∩∩
* 　　（´･ω･）
* 　 ＿|　⊃／(＿＿_
* 　／ └-(＿＿＿／
* 　￣￣￣￣￣￣￣
* 算了反正改了一个又出三个
* 　　 ⊂⌒／ヽ-、＿
* 　／⊂_/＿＿＿＿ ／
* 　￣￣￣￣￣￣￣
 */