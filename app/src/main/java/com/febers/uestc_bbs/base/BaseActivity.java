package com.febers.uestc_bbs.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.febers.uestc_bbs.R;
import com.febers.uestc_bbs.view.MyProgressDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public abstract class BaseActivity extends AppCompatActivity{

    protected MyProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
    }

    protected abstract int setView();
    protected abstract void initView();

    protected int getContentView() {
        return setView();
    }

    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new MyProgressDialog(this);
        }
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.dismiss();
    }
}
