package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean

interface PListContract {

    interface Model {
        fun pListService(fid: Int, page: Int)
        fun boardListService(fid: Int)
    }

    interface View : BaseView {
        fun showPList(event: BaseEvent<PostListBean>){}
        fun showBoardList(event: BaseEvent<BoardListBean_>){}
    }

    abstract class Presenter(view: BaseView) : BasePresenter<BaseView>(view) {
        abstract fun pListRequest(fid: Int, page: Int)
        abstract fun pListResult(event: BaseEvent<PostListBean>)

        abstract fun boardListRequest(fid: Int)
        abstract fun boardListResult(event: BaseEvent<BoardListBean_>)
    }
}