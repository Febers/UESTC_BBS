/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.view;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.febers.uestc_bbs.R;

public class CustomLoginDialog extends AlertDialog{

    private Context mContext;
    private TextInputEditText tieUserId, tieUserPw;
    private TextView tvTitle;
    private AlertDialog dialog;
    private View mView;
    private Button btCancel, btEnter;
    private OnLoginListener loginListener = null;
    private String stId = "", stPw = "";

    public CustomLoginDialog(Context mContext) {
        super(mContext, R.style.Theme_AppCompat_Dialog);
        this.mContext = mContext;
        dialog = new Builder(mContext).create();
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);
        tvTitle = mView.findViewById(R.id.tv_dialog_title);
        tieUserId = mView.findViewById(R.id.tie_user_id);
        tieUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stId = s.toString();
            }
        });
        tieUserPw = mView.findViewById(R.id.tie_user_pw);
        tieUserPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stPw = s.toString();
            }
        });
        btEnter = mView.findViewById(R.id.bt_dialog_login_enter);
        btCancel = mView.findViewById(R.id.bt_dialog_login_cancel);
        btEnter.setOnClickListener(view ->
                loginListener.onClick(view)
        );
        btCancel.setOnClickListener(view ->
                loginListener.onClick(view)
        );
        dialog.setView(mView);
    }

    public String getStId() {
        return stId;
    }

    public String getStPw() {
        return stPw;
    }

    public TextInputEditText getTieUserId() {
        return tieUserId;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setShowTitle(Boolean show) {
        if (show) {
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.INVISIBLE);
        }
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

    //定义点击监听接口
    public interface OnLoginListener {
        void onClick(View v);
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        loginListener = onLoginListener;
    }
}
