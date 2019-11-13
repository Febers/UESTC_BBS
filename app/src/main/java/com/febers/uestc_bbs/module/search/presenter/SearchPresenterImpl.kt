package com.febers.uestc_bbs.module.search.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.entity.SearchUserBean
import com.febers.uestc_bbs.http.SearchInterface
import com.febers.uestc_bbs.module.search.contract.SearchContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPresenterImpl(view: SearchContract.View): SearchContract.Presenter(view) {

    override fun searchPostRequest(keyword: String, page: Int) {
        ThreadPoolMgr.execute(Runnable { getSearchPost(keyword, page) })
    }

    private fun getSearchPost(keyword: String, page: Int) {
        val searchQuest = getRetrofit().create(SearchInterface::class.java)
        val call = searchQuest.getSearchPost(
                keyword = keyword,
                page = page.toString(),
                pageSize = COMMON_PAGE_SIZE.toString()
        )
        call.enqueue(object : Callback<SearchPostBean> {
            override fun onFailure(call: Call<SearchPostBean>?, t: Throwable?) {
                errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<SearchPostBean>?, response: Response<SearchPostBean>?) {
                val searchPostBean = response?.body()
                if (searchPostBean == null) {
                    errorResult(SERVICE_RESPONSE_NULL)
                    return
                }
                if (searchPostBean.rs != REQUEST_SUCCESS_RS) {
                    errorResult(searchPostBean.head?.errInfo.toString())
                    return
                }
                mView?.showPostSearchResult(BaseEvent(
                        if (searchPostBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                        else BaseCode.SUCCESS,
                        searchPostBean))
            }
        })
    }

    override fun searchUserRequest(keyword: String, page: Int) {
        ThreadPoolMgr.execute(Runnable { getSearchUser(keyword, page) })
    }

    private fun getSearchUser(keyword: String, page: Int) {
        getRetrofit().create(SearchInterface::class.java)
                .getSearchUser(keyword = keyword,
                        page = page.toString(),
                        pageSize = COMMON_PAGE_SIZE.toString())
                .enqueue(object : Callback<SearchUserBean> {
                    override fun onFailure(call: Call<SearchUserBean>, t: Throwable) {
                        errorResult(SERVICE_RESPONSE_ERROR)
                    }

                    override fun onResponse(call: Call<SearchUserBean>, response: Response<SearchUserBean>?) {
                        val searchUserBean = response?.body()
                        if (searchUserBean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (searchUserBean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(searchUserBean.head?.errInfo.toString())
                            return
                        }
                        mView?.showUserSearchResult(BaseEvent(
                                if (searchUserBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS,
                                searchUserBean))
                    }
                })
    }
}