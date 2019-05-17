package com.febers.uestc_bbs.module.search.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.search.contract.SearchContract
import com.febers.uestc_bbs.module.search.model.SearchModelImpl

class SearchPresenterImpl(val view: SearchContract.View): SearchContract.Presenter(view) {

    override fun searchRequest(keyword: String, page: Int) {
        val searchModel: SearchContract.Model = SearchModelImpl(this)
        searchModel.searchService(keyword, page)
    }

    override fun searchResult(event: BaseEvent<SearchPostBean>) {
        view.showSearchResult(event)
    }
}