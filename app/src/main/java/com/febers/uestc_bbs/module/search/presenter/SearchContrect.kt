package com.febers.uestc_bbs.module.search.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.SearchPostBean

interface SearchContrect {

    interface Model {
        fun searchService(keyword: String, page: Int)
    }

    interface View: BaseView {
        fun showSearchResult(event: BaseEvent<SearchPostBean>)
    }

    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(view) {
        abstract fun searchRequest(keyword: String, page: Int)
        abstract fun searchResult(event: BaseEvent<SearchPostBean>)
    }
}