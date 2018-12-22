package com.febers.uestc_bbs.module.user.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.view.*
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.UserDetailAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.DetailItemBean
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserUpdateResultBean
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.user.contract.UserContract
import com.febers.uestc_bbs.module.user.presenter.UserPresenterImpl
import com.febers.uestc_bbs.utils.*
import com.febers.uestc_bbs.view.helper.finishFail
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.dialog_update_sign.*
import org.jetbrains.anko.browse
import java.io.File

class UserDetailActivity : BaseActivity(), UserContract.View {

    private lateinit var userPresenter: UserContract.Presenter
    private var signDialog: Dialog? = null
    private lateinit var oldSign: String
    private var userItSelf = false
    private var userId: Int = 0

    override fun enableThemeHelper(): Boolean = false

    override fun setMenu(): Int? =  R.menu.menu_user_detail

    override fun setView(): Int {
        userId = intent.getIntExtra(USER_ID, 0)
        if (userId == MyApp.getUser().uid) userItSelf = true
        return R.layout.activity_user_detail
    }

    override fun setToolbar(): Toolbar? = toolbar_user_detail

    override fun initView() {
        collapsing_toolbar_layout_detail.apply {
            setStatusBarScrimColor(ThemeHelper.getColorPrimary())
            setContentScrimColor(ThemeHelper.getColorPrimary())
            setCollapsedTitleTextColor(ThemeHelper.getRefreshTextColor())
        }
        userPresenter = UserPresenterImpl(this)
        refresh_layout_user_detail.apply {
            initAttrAndBehavior()
            setOnRefreshListener { getUserDetail() }
        }
        if (userItSelf) {
//            userBottomSheet = UserDetailBottomSheet(this, R.style.PinkBottomSheetTheme)
//            userBottomSheet.setView(R.layout.layout_bottom_sheet_user_detail)
        }
        image_view_user_detail_blur_avatar.setBackgroundColor(ThemeHelper.getColorPrimary())
        signDialog = AlertDialog.Builder(this@UserDetailActivity).create()
    }

    private fun getUserDetail() {
        userPresenter.userDetailRequest(userId)
    }

    /**
     * 显示用户详情
     */
    @SuppressLint("RestrictedApi")
    override fun showUserDetail(event: BaseEvent<UserDetailBean>) {
        recyclerview_user_detail?.adapter = UserDetailAdapter(this, initUserItem(event.data)).apply {
            setOnItemClickListener { viewHolder, detailItemBean, i ->
                if (i == 0 && userItSelf) {
                    signDialog?.show()
                    signDialog?.setContentView(getSignDialogView())
                }
            }
        }
        collapsing_toolbar_layout_detail?.title = event.data.name
        ImageLoader.load(this, event.data.icon, image_view_user_detail_blur_avatar,
                placeImage = null,
                isCircle = false,
                isBlur = true,
                noCache = true,
                clickToViewer = false)
        ImageLoader.load(this, event.data.icon, image_view_user_detail_avatar,
                placeImage = null,
                isCircle = true,
                noCache = true,
                clickToViewer = false)
        image_view_user_detail_avatar.setOnClickListener {
            if (userItSelf) {
                /**
                 * 由于上传头像至河畔服务器，均提示修改头像失败，所以改为查看大图
                 * TODO 选择新头像或者查看大图
                 */
                //chooseAvatar()
                ClickContext.clickToViewAvatarByUid(userId, this@UserDetailActivity)
            } else {
                ClickContext.clickToViewAvatarByUid(userId, this@UserDetailActivity)
            }
        }
        refresh_layout_user_detail?.finishRefresh()
        if (userItSelf) return

        fab_user_detail?.let { it ->
            it.visibility = View.VISIBLE
            it.setOnClickListener { ClickContext.clickToPrivateMsg(this@UserDetailActivity, userId, event.data.name) }
        }
        linear_layout_user_post_start?.apply {
            visibility = View.VISIBLE
            setOnClickListener{
                startActivity(Intent(this@UserDetailActivity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userId)
                    putExtra(USER_POST_TYPE, USER_START_POST) })
            }
        }
        linear_layout_user_post_reply?.apply {
            visibility = View.VISIBLE
            setOnClickListener{
                startActivity(Intent(this@UserDetailActivity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userId)
                    putExtra(USER_POST_TYPE, USER_REPLY_POST) })
            }
        }
    }

    /**
     * 更新资料成功之后刷新页面
     */
    @UiThread
    override fun showUserUpdate(event: BaseEvent<UserUpdateResultBean>) {
        showHint(event.data.head?.errInfo.toString())
        signDialog?.dismiss()
        progress_bar_user_detail?.visibility = View.GONE
        refresh_layout_user_detail.autoRefresh()
        PictureFileUtils.deleteCacheDirFile(this@UserDetailActivity)
    }

    private fun initUserItem(user: UserDetailBean): List<DetailItemBean> {
        oldSign = if(user.sign.isNullOrEmpty()) "" else user.sign.toString()
        val item1 = DetailItemBean("签名", if(user.sign.isNullOrEmpty()) "签名未设置" else user.sign)
        val item2 = DetailItemBean("性别", GenderUtils.change(user.gender.toString()))
        val item3 = DetailItemBean("等级", user.userTitle)
        val item4 = DetailItemBean("积分", user.score.toString())
        val item5 = DetailItemBean("水滴", user.gold_num.toString())
        if (user.body?.profileList!!.isNotEmpty()) {

        }
        return arrayListOf(item1, item2, item3, item4, item5)
    }

    private fun getSignDialogView(): View {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_update_sign, null)
        val editText = view.findViewById<EditText>(R.id.edit_text_update_sign)
        editText.setText(oldSign)
        //使其可以弹出软键盘
        signDialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        val btnEnter = view.findViewById<Button>(R.id.btn_dialog_sign_update_enter)
        btnEnter.setTextColor(ThemeHelper.getColorPrimary())
        val btnCancel = view.findViewById<Button>(R.id.btn_dialog_sign_update_cancel)
        btnCancel.setOnClickListener {
            signDialog?.dismiss()
        }
        btnEnter.setOnClickListener {
            view.findViewById<ProgressBar>(R.id.progress_bar_update_sign).visibility = View.VISIBLE
            userPresenter.userUpdateRequest(USER_SIGN, editText.text.toString())
        }
        return view
    }

    override fun showError(msg: String) {
        runOnUiThread {
            showHint(msg)
            findViewById<ProgressBar>(R.id.progress_bar_update_sign)?.visibility = View.GONE
            progress_bar_update_sign?.visibility = View.GONE
            progress_bar_user_detail?.visibility = View.GONE
            refresh_layout_user_detail?.finishFail()
            PictureFileUtils.deleteCacheDirFile(this@UserDetailActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_user_detail_web) {
            browse("http://bbs.uestc.edu.cn/home.php?mod=space&uid=$userId")
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 选择新头像上传至服务器
     */
    private fun chooseAvatar() {
        PictureSelector.create(this@UserDetailActivity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .enableCrop(true)
                .isDragFrame(true)
                .previewImage(true)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            val newAvatarUri = selectList[0].path
//            i("PE", "path:" + newAvatarUri)
            progress_bar_user_detail?.visibility = View.VISIBLE
            userPresenter.userUpdateRequest(USER_AVATAR, newValue = File(newAvatarUri))
        }
    }

}
