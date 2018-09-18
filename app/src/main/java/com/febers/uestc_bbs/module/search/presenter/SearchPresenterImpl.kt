package com.febers.uestc_bbs.module.search.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.SearchPostBean

class SearchPresenterImpl(val view: BaseView): SearchContrect.Presenter(view) {

    override fun searchRequest(keyword: String, page: Int) {

    }

    override fun searchResult(event: BaseEvent<SearchPostBean>) {

    }
}