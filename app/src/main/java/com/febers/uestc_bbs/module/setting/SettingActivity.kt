package com.febers.uestc_bbs.module.setting

import android.content.Intent
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.io.UserHelper
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.view.LoginActivity
import com.febers.uestc_bbs.io.CacheHelper
import com.febers.uestc_bbs.module.setting.refresh_style.RefreshStyleFragment
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import com.febers.uestc_bbs.view.adapter.SimpleUserAdapter
import kotlinx.android.synthetic.main.fragment_setting.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.collections.ArrayList

class SettingActivity : BaseActivity() {

    private var users: MutableList<UserSimpleBean> = ArrayList()
    private var options: MutableList<SettingItemBean> = ArrayList()

    private lateinit var simpleUserAdapter: SimpleUserAdapter
    private lateinit var settingAdapter: SettingAdapter
    private lateinit var cacheItem: SettingItemBean
    private var refreshsStyleView: RefreshStyleFragment? = null

    private var hintWay by PreferenceUtils(MyApp.context(), HINT_WAY, HINT_BY_TOAST)

    override fun setView(): Int {
        return R.layout.fragment_setting
    }

    override fun setToolbar(): Toolbar? = toolbar_setting

    override fun registerEventBus(): Boolean = true

    override fun initView() {
        simpleUserAdapter = SimpleUserAdapter(context, users).apply {
            setOnItemClickListener { viewHolder, userItemBean, i ->
                changeUser(userItemBean)
            }
            setOnItemChildClickListener(R.id.btn_delete_user) {viewHolder, userItemBean, i ->
                deleteUser(userItemBean, i)
            }
        }
        settingAdapter = SettingAdapter(context, options).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> { onHintMethodChange() }
                    1 -> {
                        if (refreshsStyleView == null) {
                            refreshsStyleView = RefreshStyleFragment()
                        }

                        refreshsStyleView?.show(supportFragmentManager, "style")
                    }
                    2 -> { clearCache() }
                    3 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                    }
                    4 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                    }
                }
            }
        }
        recyclerview_setting_users.adapter = simpleUserAdapter
        recyclerview_setting_users.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
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
        val item0 = SettingItemBean("提示", "设置消息提示的样式")
        val item1 = SettingItemBean("刷新控件", "设置刷新控件样式")
        cacheItem = SettingItemBean(getString(R.string.clear_image_cache), "...")
        val item3 = SettingItemBean("后台接收消息", "除非强制退出，否则将一直发送心跳包查询消息", showCheck = true, checked = false)
        val item4 = SettingItemBean("缓存首页帖子", "保存首页的第一页帖子数据", showCheck = true, checked = true)
        return arrayListOf(item0, item1, cacheItem)
    }

    /**
     * 进行账户的切换，在Android 9.0的模拟器上，切换之后会重复显示一个当前用户
     * 尚不知道原因是啥，猜测问题出在adapter上，因为users的数据变化没问题
     *
     * @param user 需要切换的用户
     */
    private fun changeUser(user: UserSimpleBean) {
        if (user.uid == UserHelper.getNowUid()) return
        UserHelper.setNowUid(user.uid)
        users.clear()
        users.addAll(0, UserHelper.getAllUser())
        simpleUserAdapter.notifyDataSetChanged()

        Thread{ EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, user)) }.start()
    }

    private fun deleteUser(user: UserSimpleBean, position: Int) {
        UserHelper.deleteUser(user.uid)
        simpleUserAdapter.remove(position)
        simpleUserAdapter.notifyDataSetChanged()
        if(!UserHelper.getAllUser().isEmpty()) {
            EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, UserHelper.getAllUser().last()))
        } else {
            EventBus.getDefault().post(BaseEvent(BaseCode.FAILURE, UserSimpleBean()))
        }
    }

    private fun onHintMethodChange() {
        if (hintWay == HINT_BY_TOAST) {
            hintWay = HINT_BY_SNACK_BAR
            HintUtils.show(context, "提示方式已更改")
        } else if (hintWay == HINT_BY_SNACK_BAR) {
            hintWay = HINT_BY_TOAST
            HintUtils.show("提示方式已更改")
        }
    }

    /*
        添加用户成功之后重新获取账户列表,
        但是它可以在此Fragment内被触发，这个诡异的现象我浪费了一个多小时才弄明白
        珍惜你的时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(event: BaseEvent<UserSimpleBean>) {
        if (event.code == BaseCode.SUCCESS) {
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
        showHint("清除中")
        Thread{
            CacheHelper.clearCache()
            getCache()
        }.start()
    }
}