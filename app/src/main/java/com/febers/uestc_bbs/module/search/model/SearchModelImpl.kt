package com.febers.uestc_bbs.module.search.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.search.presenter.SearchContrect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchModelImpl(val searchPresenter: SearchContrect.Presenter): BaseModel(), SearchContrect.Model {

    override fun searchService(keyword: String, page: Int) {
        mKeyword = keyword
        mPage = page.toString()
        Thread(Runnable { getSearchPost() }).start()
    }

    private fun getSearchPost() {
        val searchQuest = getRetrofit().create(SearchInterface::class.java)
        val call = searchQuest.getSearchPost(
                accessSecret = getUser().secrete,
                accessToken = getUser().token,
                keyword = mKeyword,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE
        )
        call.enqueue(object : Callback<SearchPostBean> {
            override fun onFailure(call: Call<SearchPostBean>?, t: Throwable?) {
                searchPresenter.searchResult(BaseEvent(BaseCode.FAILURE, SearchPostBean()
                        .apply { errcode = SERVICE_RESPONSE_ERROR + t.toString() }))
            }

            override fun onResponse(call: Call<SearchPostBean>?, response: Response<SearchPostBean>?) {
                val searchPostBean = response?.body()
                if (searchPostBean == null) {
                    searchPresenter.searchResult(BaseEvent(BaseCode.FAILURE, SearchPostBean()
                            .apply { errcode = SERVICE_RESPONSE_NULL }))
                    return
                }
                if (searchPostBean.rs != REQUEST_SUCCESS_RS) {
                    searchPresenter.searchResult(BaseEvent(BaseCode.FAILURE, SearchPostBean()
                            .apply { errcode = searchPostBean.head?.errInfo }))
                    return
                }
                if (searchPostBean.has_next != HAVE_NEXT_PAGE) {
                    searchPresenter.searchResult(BaseEvent(BaseCode.SUCCESS_END, searchPostBean))
                } else {
                    searchPresenter.searchResult(BaseEvent(BaseCode.SUCCESS, searchPostBean))
                }
            }
        })
    }
}