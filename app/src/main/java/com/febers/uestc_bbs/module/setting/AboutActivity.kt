package com.febers.uestc_bbs.module.setting

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.UpdateCheckEvent
import com.febers.uestc_bbs.entity.GithubReleaseBean
import com.febers.uestc_bbs.entity.ProjectItemBean
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.io.FileHelper
import com.febers.uestc_bbs.utils.DonateUtils
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.update.UpdateDialogHelper
import com.febers.uestc_bbs.module.update.UpdateHelper
import com.febers.uestc_bbs.view.adapter.OpenProjectAdapter
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.activity_about.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.share

class AboutActivity: BaseActivity() {

    private var items1: MutableList<SettingItemBean> = ArrayList()
    private var items2: MutableList<SettingItemBean> = ArrayList()
    private var items3: MutableList<SettingItemBean> = ArrayList()
    private lateinit var settingAdapter1: SettingAdapter
    private lateinit var settingAdapter2: SettingAdapter
    private lateinit var settingAdapter3: SettingAdapter
    private var openSourceProjectsDialog: AlertDialog? = null
    private lateinit var projectAdapter: OpenProjectAdapter
    private var permissionDialog: AlertDialog? = null
    private var updateLogDialog: AlertDialog? = null

    override fun setToolbar(): Toolbar? = toolbar_about

    override fun setTitle(): String? = getString(R.string.about)

    override fun setMenu(): Int? = R.menu.menu_about

    override fun setView(): Int = R.layout.activity_about

    override fun registerEventBus(): Boolean = true

    override fun initView() {
    }

    override fun afterCreated() {
        text_view_about_1.setTextColor(ThemeHelper.getColorPrimaryBySp())
        text_view_about_2.setTextColor(ThemeHelper.getColorPrimaryBySp())
        text_view_about_3.setTextColor(ThemeHelper.getColorPrimaryBySp())

        settingAdapter1 = SettingAdapter(mContext, items1).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                onFirstGroupItemClick(i)
            }
        }
        recyclerview_about_1.adapter = settingAdapter1

        settingAdapter2 = SettingAdapter(mContext, items2).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                onSecondGroupItemClick(i)
            }
        }
        recyclerview_about_2.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(mContext, items3).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                onThirdGroupItemClick(i)
            }
        }
        recyclerview_about_3.adapter = settingAdapter3

        items1.addAll(initSettingData1())
        settingAdapter1.notifyDataSetChanged()
        items2.addAll(initSettingData2())
        settingAdapter2.notifyDataSetChanged()
        items3.addAll(initSettingData3())
        settingAdapter3.notifyDataSetChanged()

        openSourceProjectsDialog = AlertDialog.Builder(mContext)
                .create()
        projectAdapter = OpenProjectAdapter(mContext, initOpenProjectData()).apply {
            setOnItemClickListener { viewHolder, projectItemBean, i ->
                context.browse(url = "https://github.com/" + projectItemBean.author + "/" + projectItemBean.name)
            }
        }
        btn_share_app.setOnClickListener {
            share("""
                欢迎使用清水河畔开源客户端“i河畔”
                下载地址：https://www.coolapk.com/apk/com.febers.uestc_bbs
                开源地址：https://github.com/Febers/UESTC_BBS

            """.trimIndent())
        }
        btn_share_app.setTextColor(ThemeHelper.getColorPrimaryBySp())
    }

    private fun initSettingData1(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.version), getString(R.string.version_value))
        val item2 = SettingItemBean(getString(R.string.check_update), getString(R.string.version_value))
        val item3 = SettingItemBean(getString(R.string.update_log), getString(R.string.check_update_log))
        return arrayListOf(item2, item3)
    }

    private fun initSettingData2(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.developer_name), getString(R.string.click_to_developer_bbs_account))
        val item2 = SettingItemBean(getString(R.string.feedback_and_other), getString(R.string.developer_email))
        val item3 = SettingItemBean(getString(R.string.donate), getString(R.string.treat_the_developer_for_a_coke))
        return arrayListOf(item1, item2, item3)
    }

    private fun initSettingData3(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.source_code), "Apache License 2.0")
        val item2 = SettingItemBean(getString(R.string.open_source_project), getString(R.string.click_to_the_open_source_projects_using))
        return arrayListOf(item1, item2)
    }

    private fun onFirstGroupItemClick(position: Int) {
        when(position) {
            0 -> {
                UpdateHelper.check(manual = true)
                showHint("正在检查更新")
            }
            1 -> {
                if (updateLogDialog == null) {
                    updateLogDialog = AlertDialog.Builder(mContext)
                            .setTitle(getString(R.string.update_log))
                            .setMessage(Html.fromHtml(FileHelper.getAssetsString(mContext, "update_log.html")))
                            .setPositiveButton(getString(R.string.confirm)) { dialog, which ->
                                updateLogDialog?.dismiss()
                            }
                            .create()
                }
                updateLogDialog?.show()
            }
        }
    }

    private fun onSecondGroupItemClick(position: Int) {
        when(position) {
            0 -> {
                ClickContext.clickToUserDetail(context = mContext, uid = 196486)
            }
            1 -> {
                mContext.email(getString(R.string.developer_email))
            }
            2 -> {
                DonateUtils(mContext).donateByAlipay()
            }
        }
    }

    private fun onThirdGroupItemClick(position: Int) {
        when(position) {
            0 -> {
                mContext.browse(getString(R.string.app_project_url))
            }
            1 -> {
                openSourceProjectsDialog?.show()
                openSourceProjectsDialog?.setContentView(getOpenSourceProjects())
            }
        }
    }

    private fun getOpenSourceProjects(): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_open_projects, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_open_source)
        val btn = view.findViewById<Button>(R.id.btn_dialog_open_prj_enter)
        btn.setOnClickListener {
            openSourceProjectsDialog?.dismiss()
        }
        recyclerView.adapter = projectAdapter
        return view
    }

    private fun initOpenProjectData(): List<ProjectItemBean> = arrayListOf(
            ProjectItemBean("aesthetic", "afollestad", ""),
            ProjectItemBean("ahbottomnavigation", "aurelhubert", ""),
            ProjectItemBean("anko", "Kotlin", ""),
            ProjectItemBean("EventBus", "greenrobot", ""),
            ProjectItemBean("Fragmentation", "YoKeyword", ""),
            ProjectItemBean("glide", "bumptech", ""),
            ProjectItemBean("glide-transformations", "wasabeef", ""),
            ProjectItemBean("HoloColorPicker", "LarsWerkman", ""),
            ProjectItemBean("PandaEmoView", "PandaQAQ", ""),
            ProjectItemBean("PictureSelector", "LuckSiege", ""),
            ProjectItemBean("RecyclerViewAdapter", "SheHuan", ""),
            ProjectItemBean("retrofit", "square", "")

    )

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_permission) {
            if (permissionDialog == null) {
                permissionDialog = AlertDialog.Builder(mContext)
                        .setMessage(Html.fromHtml(FileHelper.getAssetsString(mContext, "permission_explain.html")))
                        .setPositiveButton(getString(R.string.confirm)) { p0, p1 -> }
                        .create()
            }

            permissionDialog?.show()
        }
        return super.onOptionsItemSelected(item)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCheckUpdateResult(event: UpdateCheckEvent) {
        when(event.code) {
            BaseCode.FAILURE -> showHint("检查更新失败，请前往开源界面查看最新版本")
            BaseCode.LOCAL -> showHint("当前已是最新版本")
            BaseCode.SUCCESS_END -> { showUpdateDialog(event.result!!) }
            else -> { }
        }
    }

    private fun showUpdateDialog(githubReleaseBean: GithubReleaseBean) {
        val dialogHelper = UpdateDialogHelper(mContext)
        dialogHelper.showGithubUpdateDialog(githubReleaseBean)
    }
}