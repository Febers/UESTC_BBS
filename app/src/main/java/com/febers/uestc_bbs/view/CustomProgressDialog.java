/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.febers.uestc_bbs.R;


public class CustomProgressDialog extends AlertDialog {
    private Context mContext;
    private View view;
    private AlertDialog dialog;
    private ProgressBar progressBar;

    public CustomProgressDialog(Context context) {
        this(context, "请稍侯");
    }

    public CustomProgressDialog(Context context, String title) {
        super(context, R.style.Theme_AppCompat_Dialog);
        this.mContext = context;
        dialog = new Builder(mContext).create();
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
        progressBar = view.findViewById(R.id.pb_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.setView(view);
    }

    public void show() {
        dialog.show();
    }
    public void hide() {
        dialog.hide();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }
}
