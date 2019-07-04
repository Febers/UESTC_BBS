package com.febers.uestc_bbs.module.search.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.entity.SearchUserBean
import com.febers.uestc_bbs.module.search.contract.SearchContract
import com.febers.uestc_bbs.module.search.model.SearchModelImpl

class SearchPresenterImpl(val view: SearchContract.View): SearchContract.Presenter(view) {

    override fun searchPostRequest(keyword: String, page: Int) {
        val searchModel: SearchContract.Model = SearchModelImpl(this)
        searchModel.searchPostService(keyword, page)
    }

    override fun searchPostResult(event: BaseEvent<SearchPostBean>) {
        view.showPostSearchResult(event)
    }

    override fun searchUserRequest(keyword: String, page: Int) {
        val searchModel: SearchContract.Model = SearchModelImpl(this)
        searchModel.searchUserService(keyword, page)
    }

    override fun searchUserResult(event: BaseEvent<SearchUserBean>) {
        view.showUserSearchResult(event)
    }
}