package com.febers.uestc_bbs.mvp;

public interface IView<T> {
    void setPresenter(T Presenter);
    void showError(String errorMsg);
}
