package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.model.PListModelImpl

class PListPresenterImpl(var view: PListContract.View) : PListContract.Presenter(view) {


    override fun pListRequest(fid: Int, page: Int) {
        val pListModel: PListContract.Model = PListModelImpl(this)
        pListModel.pListService(fid, page)
    }

    override fun pListResult(event: BaseEvent<PostListBean>) {
        view.showPList(event)
    }

    override fun boardListRequest(fid: Int) {
        val pListModel: PListContract.Model = PListModelImpl(this)
        pListModel.boardListService(fid)
    }

    override fun boardListResult(event: BaseEvent<BoardListBean_>) {
        view.showBoardList(event)
    }
}
