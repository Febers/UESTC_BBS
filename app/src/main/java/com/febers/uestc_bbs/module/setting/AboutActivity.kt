package com.febers.uestc_bbs.module.setting

import android.text.Html
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.entity.ProjectItemBean
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.io.FileHelper
import com.febers.uestc_bbs.utils.DonateUtils
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.view.adapter.OpenProjectAdapter
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.fragment_about.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email

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

    override fun setView(): Int {
        return R.layout.fragment_about
    }

    override fun initView() {
    }

    override fun afterCreated() {
        settingAdapter1 = SettingAdapter(context, items1).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                onFirstGroupItemClick(i)
            }
        }
        recyclerview_about_1.adapter = settingAdapter1

        settingAdapter2 = SettingAdapter(context, items2).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                onSecondGroupItemClick(i)
            }
        }
        recyclerview_about_2.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(context, items3).apply {
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

        openSourceProjectsDialog = AlertDialog.Builder(context)
                .create()
        projectAdapter = OpenProjectAdapter(context, initOpenProjectData()).apply {
            setOnItemClickListener { viewHolder, projectItemBean, i ->
                context.browse(url = "https://github.com/" + projectItemBean.author + "/" + projectItemBean.name)
            }
        }
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
                Beta.checkUpgrade()
            }
            1 -> {
                if (updateLogDialog == null) {
                    updateLogDialog = AlertDialog.Builder(context)
                            .setTitle(getString(R.string.update_log))
                            .setMessage(Html.fromHtml(FileHelper.getAssetsString(context, "update_log.html")))
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
                ClickContext.clickToUserDetail(context = context, uid = 196486)
            }
            1 -> {
                context.email(getString(R.string.developer_email))
            }
            2 -> {
                DonateUtils(context).donateByAlipay()
            }
        }
    }

    private fun onThirdGroupItemClick(position: Int) {
        when(position) {
            0 -> {
                context.browse(getString(R.string.app_project_url))
            }
            1 -> {
                openSourceProjectsDialog?.show()
                openSourceProjectsDialog?.setContentView(getOpenSourceProjects())
            }
        }
    }

    private fun getOpenSourceProjects(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_open_projects, null)
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
//        if (item?.itemId == R.id.menu_item_donate) {
//            DonateUtils(context).donateByAlipay()
//        }
        if (item?.itemId == R.id.menu_item_permission) {
            if (permissionDialog == null) {
                permissionDialog = AlertDialog.Builder(context)
                        .setMessage(Html.fromHtml(FileHelper.getAssetsString(context, "permission_explain.html")))
                        .setPositiveButton(getString(R.string.confirm)) { p0, p1 -> }
                        .create()
            }

            permissionDialog?.show()
        }
        return super.onOptionsItemSelected(item)
    }
}