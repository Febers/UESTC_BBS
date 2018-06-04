package com.febers.uestc_bbs.post;

import android.util.Log;

public class PostPresenter extends PostContract.Presenter{

    private PostContract.View mView;
    private static final String TAG = "PostPresenter";

    public PostPresenter(PostContract.View view) {
        super(view);
        mView = view;
    }

    @Override
    public void getPosts(int position) {
        Log.i(TAG, "getPosts: " + position);
    }
}
