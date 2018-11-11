package com.febers.uestc_bbs.module.user.view.bottom_sheet

import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.ImageLoader
import kotlinx.android.synthetic.main.layout_bottom_sheet_user_detail.*

class UserDetailBottomSheet(val activity: Activity, style: Int): BottomSheetDialog(activity, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = MyApp.getUser()
        ImageLoader.load(context, user.avatar, image_view_user_edit_avatar)
        image_view_user_edit_avatar.setOnClickListener {

        }

        btn_user_edit_enter.apply {
            setTextColor(ThemeHelper.getColorPrimary())
        }
    }
}