package com.febers.uestc_bbs.module.search.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.search.model.SearchModelImpl

class SearchPresenterImpl(val view: SearchContrect.View): SearchContrect.Presenter(view) {

    override fun searchRequest(keyword: String, page: Int) {
        val searchModel: SearchContrect.Model = SearchModelImpl(this)
        searchModel.searchService(keyword, page)
    }

    override fun searchResult(event: BaseEvent<SearchPostBean>) {
        view.showSearchResult(event)
    }
}