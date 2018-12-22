package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean

interface PListContract {

    interface Model {
        fun pListService(fid: Int, page: Int, pageSize: Int = COMMON_PAGE_SIZE, filterType: String = PLIST_SORT_BY_TYPE, filterId: Int = 0)
        fun boardListService(fid: Int)
    }

    interface View : BaseView {
        fun showPList(event: BaseEvent<PostListBean>){}
        fun showBoardList(event: BaseEvent<BoardListBean_>){}
    }

    abstract class Presenter(view: BaseView) : BasePresenter<BaseView>(view) {
        abstract fun pListRequest(fid: Int, page: Int, pageSize:Int = COMMON_PAGE_SIZE, filterType: String = PLIST_SORT_BY_TYPE, filterId: Int = 0)
        abstract fun pListResult(event: BaseEvent<PostListBean>)

        abstract fun boardListRequest(fid: Int)
        abstract fun boardListResult(event: BaseEvent<BoardListBean_>)
    }
}