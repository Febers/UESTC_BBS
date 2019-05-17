package com.febers.uestc_bbs.module.search.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.search.contract.SearchContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchModelImpl(val searchPresenter: SearchContract.Presenter): BaseModel(), SearchContract.Model {

    override fun searchService(keyword: String, page: Int) {
        mKeyword = keyword
        mPage = page.toString()
        Thread(Runnable { getSearchPost() }).start()
    }

    private fun getSearchPost() {
        val searchQuest = getRetrofit().create(SearchInterface::class.java)
        val call = searchQuest.getSearchPost(
                keyword = mKeyword,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE.toString()
        )
        call.enqueue(object : Callback<SearchPostBean> {
            override fun onFailure(call: Call<SearchPostBean>?, t: Throwable?) {
                searchPresenter.errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<SearchPostBean>?, response: Response<SearchPostBean>?) {
                val searchPostBean = response?.body()
                if (searchPostBean == null) {
                    searchPresenter.errorResult(SERVICE_RESPONSE_NULL)
                    return
                }
                if (searchPostBean.rs != REQUEST_SUCCESS_RS) {
                    searchPresenter.errorResult(searchPostBean.head?.errInfo.toString())
                    return
                }
                searchPresenter.searchResult(BaseEvent(
                        if (searchPostBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                        else BaseCode.SUCCESS,
                        searchPostBean))
            }
        })
    }
}