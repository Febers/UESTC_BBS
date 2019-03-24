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
import com.febers.uestc_bbs.module.service.HeartMsgService
import com.febers.uestc_bbs.module.setting.refresh_style.RefreshStyleFragment
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.log
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
    private var refreshStyleView: RefreshStyleFragment? = null
    private var iconChooseView: IconFragment? = null

    private var hintWay by PreferenceUtils(MyApp.context(), HINT_WAY, HINT_BY_TOAST)
    private var loopReceiveMsg by PreferenceUtils(MyApp.context(), LOOP_RECEIVE_MSG, true)

    override fun setView(): Int {
        return R.layout.fragment_setting
    }

    override fun setToolbar(): Toolbar? = toolbar_setting

    override fun setTitle(): String? = getString(R.string.setting_and_account)

    override fun registerEventBus(): Boolean = true

    override fun initView() {
    }

    override fun afterCreated() {
        init()
    }

    private fun init() {
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
                        if (iconChooseView == null) {
                            iconChooseView = IconFragment()
                        }
                        iconChooseView?.show(supportFragmentManager, "icon")
                    }
                    2 -> {
                        if (refreshStyleView == null) {
                            refreshStyleView = RefreshStyleFragment()
                        }
                        refreshStyleView?.show(supportFragmentManager, "style")
                    }
                    3 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        onReceiveMsgChange(!checkBox.isChecked)
                    }
                    4 -> { clearCache() }
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
        val item0 = SettingItemBean(getString(R.string.hint), getString(R.string.set_hint_style))
        val item1 = SettingItemBean(getString(R.string.icon), getString(R.string.icon_style_in_launcher))
        val item2 = SettingItemBean(getString(R.string.refresh_style), getString(R.string.choose_refresh_style))
        val item3 = SettingItemBean(getString(R.string.no_disturbing), getString(R.string.no_disturbing_explain), showCheck = true, checked = !loopReceiveMsg)
        cacheItem = SettingItemBean(getString(R.string.clear_cache), "...")
        return arrayListOf(item0, item1, item2, item3, cacheItem)
    }

    /**
     * 进行账户的切换，在Android 8.0的模拟器上，切换之后会重复显示一个当前用户
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
        Thread{ EventBus.getDefault().post(BaseEvent(BaseCode.LOCAL, user)) }.start()
    }

    private fun deleteUser(user: UserSimpleBean, position: Int) {
        UserHelper.deleteUser(user.uid)
        users.removeAt(position)
        simpleUserAdapter.notifyDataSetChanged()

        if(!UserHelper.getAllUser().isEmpty()) {
            EventBus.getDefault().post(BaseEvent(BaseCode.LOCAL, UserHelper.getAllUser().last()))
        } else {
            EventBus.getDefault().post(BaseEvent(BaseCode.FAILURE, UserSimpleBean()))
        }
    }

    private fun onHintMethodChange() {
        if (hintWay == HINT_BY_TOAST) {
            hintWay = HINT_BY_SNACK_BAR
            HintUtils.show(context, getString(R.string.hint_way_changed))
        } else if (hintWay == HINT_BY_SNACK_BAR) {
            hintWay = HINT_BY_TOAST
            HintUtils.show(getString(R.string.hint_way_changed))
        }
    }

    private fun onReceiveMsgChange(isLoop: Boolean) {
        loopReceiveMsg = isLoop
        val intent = Intent(context, HeartMsgService::class.java)
        if (loopReceiveMsg) {
            startService(intent)
        } else {
            stopService(intent)
        }
    }

    /*
        添加用户成功之后重新获取账户列表,
        但是它可以在此Fragment内被触发，这个诡异的现象我浪费了一个多小时才弄明白
        珍惜你的时间

        补充：我又一次在这上面浪费了一个星期，可以看到前面的changeUser 方法上的注释就是愚蠢的思考
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
            cacheItem.tip = CacheHelper.CacheSize
            runOnUiThread {
                settingAdapter.notifyItemChanged(4)
            }
        }.start()
    }

    private fun clearCache() {
        showHint(getString(R.string.cleaning))
        Thread{
            CacheHelper.clearCache()
            getCache()
        }.start()
    }
}