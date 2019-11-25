package com.febers.uestc_bbs.module.search.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.mvp.BasePresenter
import com.febers.uestc_bbs.base.mvp.BaseView
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.entity.SearchUserBean

interface SearchContract {

    interface View: BaseView {
        fun showPostSearchResult(event: BaseEvent<SearchPostBean>) {}
        fun showUserSearchResult(event: BaseEvent<SearchUserBean>) {}
    }

    abstract class Presenter(view: View): BasePresenter<View>(view) {
        abstract fun searchPostRequest(keyword: String, page: Int)
        abstract fun searchUserRequest(keyword: String, page: Int)
    }
}