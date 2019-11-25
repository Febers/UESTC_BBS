package com.febers.uestc_bbs.module.setting

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.UserUpdateEvent
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.io.UserManager
import com.febers.uestc_bbs.module.login.view.LoginActivity
import com.febers.uestc_bbs.utils.colorAccent
import com.febers.uestc_bbs.utils.logi
import com.febers.uestc_bbs.utils.postSticky
import com.febers.uestc_bbs.view.adapter.SimpleUserAdapter
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AccountActivity: BaseActivity() {

    private var users: MutableList<UserSimpleBean> = ArrayList()
    private var simpleUserAdapter: SimpleUserAdapter? = null

    override fun setView(): Int = R.layout.activity_account

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun setTitle(): String? = getString(R.string.account)

    override fun registerEventBus(): Boolean = true

    override fun initView() {
        users.addAll(UserManager.getAllUser())
        users.forEach {
            logi { "User: $it" }
        }
        simpleUserAdapter = SimpleUserAdapter(mContext, users).apply {
            setOnItemClickListener { viewHolder, userItemBean, i ->
                changeUser(userItemBean)
            }
            setOnItemChildClickListener(R.id.btn_delete_user) {viewHolder, userItemBean, i ->
                AlertDialog.Builder(mContext).setTitle("提示")
                        .setMessage("是否注销账户: ${userItemBean.name}")
                        .setPositiveButton("注销") { dialog, which ->
                            deleteUser(userItemBean, i)
                            dialog.dismiss()
                        }
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                        .show()

            }
        }
        recyclerview_accounts.adapter = simpleUserAdapter
        logi { "user adapter size: ${simpleUserAdapter?.itemCount}" }
        btn_add_user.setTextColor(colorAccent())
        btn_add_user.setOnClickListener {
            startActivity(Intent(mContext, LoginActivity::class.java))
        }
    }

    private fun changeUser(user: UserSimpleBean) {
        if (user.uid == UserManager.getNowUid()) return
        UserManager.setNowUid(user.uid)
        users.clear()
        users.addAll(0, UserManager.getAllUser())
        simpleUserAdapter?.notifyDataSetChanged()
        postSticky(UserUpdateEvent(BaseCode.LOCAL, user))
    }

    private fun deleteUser(user: UserSimpleBean, position: Int) {
        UserManager.deleteUser(user.uid)
        users.removeAt(position)
        simpleUserAdapter?.notifyDataSetChanged()

        if(UserManager.getAllUser().isNotEmpty()) {
            postSticky(UserUpdateEvent(BaseCode.LOCAL, UserManager.getAllUser().last()))
        } else {
            postSticky(UserUpdateEvent(BaseCode.FAILURE, UserSimpleBean()))
        }
    }

    /**
     * 当在账户界面打开登录界面添加用户之后，由于sticky设置为true，
     * 第一次返回账户界面和再一次打开账户界面，
     * 都会触发该事件，造成列表显示重复的问题
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onLoginSuccess(event: UserUpdateEvent) {
        if (event.code == BaseCode.SUCCESS && !users.contains(event.user)) {
            logi { "账户Activity收到用户登录的消息：${event.user.name}" }
            users.add(event.user)
            simpleUserAdapter?.notifyDataSetChanged()
        }
    }
}