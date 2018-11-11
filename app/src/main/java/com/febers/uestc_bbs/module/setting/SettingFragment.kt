package com.febers.uestc_bbs.module.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import com.febers.uestc_bbs.io.UserHelper
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.view.LoginActivity
import com.febers.uestc_bbs.module.setting.refresh_style.RefreshStyleFragment
import com.febers.uestc_bbs.io.CacheHelper
import com.febers.uestc_bbs.io.DownloadHelper
import com.febers.uestc_bbs.io.FileHelper
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import com.febers.uestc_bbs.view.adapter.SimpleUserAdapter
import kotlinx.android.synthetic.main.fragment_setting.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.runOnUiThread
import java.io.File
import java.util.*

class SettingFragment : BaseSwipeFragment() {

    private var users: MutableList<UserSimpleBean> = LinkedList()
    private var options: MutableList<SettingItemBean> = ArrayList()

    private lateinit var simpleUserAdapter: SimpleUserAdapter
    private lateinit var settingAdapter: SettingAdapter
    private lateinit var cacheItem: SettingItemBean

    override fun setContentView(): Int {
        return R.layout.fragment_setting
    }

    override fun setToolbar(): Toolbar? = toolbar_setting

    override fun registerEventBus(): Boolean = true

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        simpleUserAdapter = SimpleUserAdapter(context!!, users).apply {
            setOnItemClickListener { viewHolder, userItemBean, i ->
                changeUser(userItemBean)
            }
            setOnItemChildClickListener(R.id.btn_delete_user) {viewHolder, userItemBean, i ->
                deleteUser(userItemBean, i)
            }
        }
        settingAdapter = SettingAdapter(context!!, options).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> { start(RefreshStyleFragment.newInstance()) }
                    1 -> { clearCache() }
                    2 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                    }
                    3 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                    }
                }
            }
        }
        recyclerview_setting_users.adapter = simpleUserAdapter
        recyclerview_setting_users.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        recyclerview_setting_option.adapter = settingAdapter
        users.addAll(UserHelper.getAllUser())
        simpleUserAdapter.notifyDataSetChanged()

        options.addAll(initSettingData())
        settingAdapter.notifyDataSetChanged()
        btn_setting_add_user.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        getCache()
    }

    private fun initSettingData(): List<SettingItemBean> {
        val item1 = SettingItemBean("刷新控件", "设置刷新控件样式")
        cacheItem = SettingItemBean(getString(R.string.clear_image_cache), "...")
        val item3 = SettingItemBean("后台接收消息", "除非强制退出，否则将一直发送心跳包查询消息", showCheck = true, checked = false)
        val item4 = SettingItemBean("缓存首页帖子", "保存首页的第一页帖子数据", showCheck = true, checked = true)
        return arrayListOf(item1, cacheItem)
    }

    private fun changeUser(user: UserSimpleBean) {
        if (user.uid == UserHelper.getNowUid()) return
        UserHelper.setNowUid(user.uid)
        users.clear()
        users.addAll(UserHelper.getAllUser())
        simpleUserAdapter.notifyDataSetChanged()
        Thread{ EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, user)) }.start()
    }

    private fun deleteUser(user: UserSimpleBean, position: Int) {
        UserHelper.delete(user.uid)
        simpleUserAdapter.remove(position)
        simpleUserAdapter.notifyDataSetChanged()
        EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, UserHelper.getAllUser().last()))
    }

    /*
        添加用户成功之后重新获取账户列表,
        但是它可以在此Fragment内被触发，这个诡异的现象我浪费了一个多小时才弄明白
        珍惜你的时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(event: BaseEvent<UserSimpleBean>) {
        if (event.code == BaseCode.SUCCESS && !isSupportVisible) {
            users.add(event.data)
            simpleUserAdapter.notifyDataSetChanged()
        }
    }

    private fun getCache() {
        Thread {
            cacheItem.tip = CacheHelper.imageCacheSize
            context?.runOnUiThread {
                settingAdapter.notifyItemChanged(1)
            }
        }.start()
    }

    private fun clearCache() {
        showToast("清除中")
        Thread{
            CacheHelper.clearCache()
            getCache()
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean): SettingFragment = SettingFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
            }
        }
    }
}