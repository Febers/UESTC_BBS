package com.febers.uestc_bbs.module.search.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.FID
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.search.presenter.SearchContrect
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: BaseSwipeFragment(), SearchContrect.View {

    override fun setToolbar(): Toolbar? {
        return toolbar_search
    }

    override fun setContentView(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {
    }

    @UiThread
    override fun showSearchResult(event: BaseEvent<SearchPostBean>) {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search_fragment, menu)
        val menuItem = menu?.findItem(R.id.item_search)
        val searchView: SearchView = menuItem?.actionView as SearchView
        searchView.isIconified = false
        searchView.queryHint = "请输入"
    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}