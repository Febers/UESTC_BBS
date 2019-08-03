package com.febers.uestc_bbs.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.febers.uestc_bbs.module.search.view.SearchPostFragment
import com.febers.uestc_bbs.module.search.view.SearchUserFragment


const val SEARCH_TYPE_POST = 0
const val SEARCH_TYPE_USER = 1

class SearchPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return if (position == SEARCH_TYPE_POST) {
            SearchPostFragment()
        } else {
            SearchUserFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == SEARCH_TYPE_POST) {
            "帖子"
        } else {
            "用户"
        }
    }

    override fun getCount(): Int = 2
}