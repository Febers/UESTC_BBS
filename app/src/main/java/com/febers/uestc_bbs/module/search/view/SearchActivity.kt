package com.febers.uestc_bbs.module.search.view

import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.SearchSubmitEvent
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.utils.KeyboardUtils
import com.febers.uestc_bbs.utils.postEvent
import com.febers.uestc_bbs.view.adapter.SearchPagerAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class SearchActivity: BaseActivity() {

    private lateinit var searchView: SearchView
    private var menuItem: MenuItem? = null
    private var inited: Boolean = false
    private var keyword = ""
    private var page = 0

    override fun setView(): Int = R.layout.activity_search

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun setTitle(): String? = getString(R.string.search)

    override fun initView() {
        val searchPagerAdapter = SearchPagerAdapter(supportFragmentManager)
        view_pager_search.adapter = searchPagerAdapter
        tab_layout_search.setupWithViewPager(view_pager_search)
        tab_layout_search.setSelectedTabIndicatorColor(ThemeManager.colorAccent())
    }

    /**
     * 将toolbar和searchView结合起来
     * 实际是将后者作为一个menuItem
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_fragment, menu)
        menuItem = menu?.findItem(R.id.menu_item_search_search_fragment)
        menuItem?.isChecked = true
        searchView = menuItem?.actionView as SearchView
        //去除下划线
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)?.setBackgroundColor(Color.TRANSPARENT)
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty() || query.trim().isEmpty()) {
                    return true
                }
                keyword = query
                page = 1
                KeyboardUtils.closeKeyboard(searchView, mContext)
                postEvent(SearchSubmitEvent(view_pager_search.currentItem, keyword))
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {

                }
                return true
            }
        }
        searchView.apply {
            isIconified = false
            queryHint = context.getString(R.string.search_content)
            setOnQueryTextListener(listener)
        }
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && inited && keyword.isNotEmpty()) {
            searchView.clearFocus()
            KeyboardUtils.closeKeyboard(searchView, this)
        }
        inited = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!KeyboardUtils.isInputViewShow(this)) {
            finish()
        }
    }
}
