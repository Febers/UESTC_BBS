package com.febers.uestc_bbs.home

import android.util.Log.d
import android.view.View
import android.widget.CompoundButton
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast

class HomeActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener, Drawer.OnDrawerItemClickListener, OnCheckedChangeListener{

    var homeDrawer: Drawer? = null

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initView() {

        val acountHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                //添加用户
                .addProfiles(
                        ProfileDrawerItem()
                                .withName("Mike Penz")
                                .withEmail("mikepenz@gmail.com")
                                .withIcon(R.drawable.ic_android_blue_24dp)
                )
                .addProfiles(
                        ProfileDrawerItem()
                                .withName("Second")
                                .withEmail("second@gmail.com")
                                .withIcon(R.drawable.ic_person_white_24dp)
                )
                //头部点击事件
                .withOnAccountHeaderSelectionViewClickListener { view, profile -> false }
                //头部箭头点击事件
                .withOnAccountHeaderListener { view, profile, current ->
                    d("here", "${profile.name}")
                    false
                }
                //头像点击
                .withOnAccountHeaderProfileImageListener(object : AccountHeader.OnAccountHeaderProfileImageListener {
                    override fun onProfileImageClick(view: View, profile: IProfile<*>, current: Boolean): Boolean {
                        d("here", "click image")
                        return false
                    }

                    override fun onProfileImageLongClick(view: View, profile: IProfile<*>, current: Boolean): Boolean {
                        return false
                    }
                })
                .build()

        homeDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar_home)
                .withAccountHeader(acountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        SectionDrawerItem()
                                .withName("分组"),
                        PrimaryDrawerItem()
                                .withName(getString(R.string.account_detail))
                                .withIcon(R.drawable.ic_android_blue_24dp)
                                .withIdentifier(0),
                        PrimaryDrawerItem()
                                .withName(getString(R.string.account_detail))
                                .withIcon(R.drawable.ic_android_blue_24dp)
                                .withIdentifier(1),
                        SwitchDrawerItem()
                                .withSwitchEnabled(true)
                                .withName("开关")
                                .withIcon(R.drawable.ic_person_white_24dp)
                                .withCheckable(false)
                                .withOnCheckedChangeListener(this))
                .withOnDrawerItemClickListener(this)
                .build()
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED)
        bottom_navigation_bar.addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, getString(R.string.app_name)))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, getString(R.string.app_name)))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, getString(R.string.app_name)))
                .addItem(BottomNavigationItem(R.drawable.navigation_empty_icon, getString(R.string.app_name)))
                .setFirstSelectedPosition(0)
                .initialise()
        bottom_navigation_bar.setTabSelectedListener(this)
    }

    override fun onTabSelected(position: Int) {
        when(position) {
            0 -> null_view_home.visibility = View.VISIBLE
            1 -> null_view_home.visibility = View.GONE
            else -> toast("you click no 0 or 1")
        }
    }

    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
        when(position) {
            1 -> d("here click", "$position")
            2 -> d("here click", "$position")
            else -> return true
        }
        //关闭drawer
        return false
    }

    override fun onCheckedChanged(drawerItem: IDrawerItem<*, *>?, buttonView: CompoundButton?, isChecked: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTabReselected(position: Int) {

    }

    override fun onTabUnselected(position: Int) {

    }

    override fun getContentView(): Int {
        return super.getContentView()
    }
}
