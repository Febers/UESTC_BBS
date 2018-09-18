package com.febers.uestc_bbs.module.search.model

import com.febers.uestc_bbs.base.BaseModel
import com.febers.uestc_bbs.module.search.presenter.SearchContrect

class SearchModelImpl: BaseModel(), SearchContrect.Model {

    override fun searchService(keyword: String, page: Int) {

    }
}