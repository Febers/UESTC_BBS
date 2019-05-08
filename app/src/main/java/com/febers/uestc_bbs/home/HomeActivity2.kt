package com.febers.uestc_bbs.home

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.ActivityMgr
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.DAY_NIGHT_THEME_CHANGE
import com.febers.uestc_bbs.module.search.view.SearchActivity
import kotlinx.android.synthetic.main.activity_home_2.*
import me.yokeyword.fragmentation.ISupportFragment

class HomeActivity2: BaseActivity() {

    private var mFragments : MutableList<ISupportFragment> = ArrayList()

    override fun setView(): Int = R.layout.activity_home_2

    override fun setToolbar(): Toolbar? = toolbar_home_2

    override fun setMenu(): Int? = R.menu.menu_home_2

    override fun setTitle(): String? = getString(R.string.app_name)

    override fun initView() {
        initToolbar()
        val firstFragment: ISupportFragment? = findFragment(HomeFirstContainer::class.java)
        if (firstFragment == null) {
            with(mFragments) {
                add(PAGE_POSITION_HOME, HomeFirstContainer())
                add(PAGE_POSITION_BLOCK, HomeSecondContainer())
                add(PAGE_POSITION_MESSAGE, HomeThirdContainer())
            }
            loadMultipleRootFragment(R.id.container_home_2, 0,
                    mFragments[0], mFragments[1], mFragments[2])
        } else {
            with(mFragments) {
                add(PAGE_POSITION_HOME, firstFragment)
                add(PAGE_POSITION_BLOCK, findFragment(HomeSecondContainer::class.java))
                add(PAGE_POSITION_MESSAGE, findFragment(HomeThirdContainer::class.java))
            }
        }
        onThemeChanged()
    }

    private fun initToolbar() {
        val toggle = ActionBarDrawerToggle(this@HomeActivity2,
                drawer_layout_home_2, toolbar_home_2,
                R.string.search, R.string.search)
        toggle.syncState()
        drawer_layout_home_2.addDrawerListener(toggle)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_item_search_home_2) {
            startActivity(Intent(this@HomeActivity2, SearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onThemeChanged() {
        val themeChanged: Boolean = intent.getBooleanExtra(DAY_NIGHT_THEME_CHANGE, false)
        if (themeChanged) {
            ActivityMgr.removeAllActivitiesExceptOne(this@HomeActivity2)
        }
    }
}