package com.febers.uestc_bbs.module.search.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.entity.SearchUserBean

interface SearchContract {

    interface Model {
        fun searchPostService(keyword: String, page: Int)
        fun searchUserService(keyword: String, page: Int)
    }

    interface View: BaseView {
        fun showPostSearchResult(event: BaseEvent<SearchPostBean>) {}
        fun showUserSearchResult(event: BaseEvent<SearchUserBean>) {}
    }

    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(view) {
        abstract fun searchPostRequest(keyword: String, page: Int)
        abstract fun searchPostResult(event: BaseEvent<SearchPostBean>)

        abstract fun searchUserRequest(keyword: String, page: Int)
        abstract fun searchUserResult(event: BaseEvent<SearchUserBean>)
    }
}