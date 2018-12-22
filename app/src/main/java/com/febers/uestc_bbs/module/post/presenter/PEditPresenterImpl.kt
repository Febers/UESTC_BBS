package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.module.post.contract.PEditContract
import com.febers.uestc_bbs.module.post.model.PEditModelImpl

class PEditPresenterImpl(val view: PEditContract.View): PEditContract.Presenter(view) {

    private val pEditModel: PEditContract.Model = PEditModelImpl(this)

    override fun newPostRequest(fid: Int, aid: String, typeId:Int, title: String, anonymous: Int, onlyAuthor: Int, vararg contents: Pair<Int, String>) {
        pEditModel.newPostService(fid, aid, typeId, title, anonymous, onlyAuthor, *contents)
    }

    override fun newPostResult(event: PostSendResultBean) {
        view.showNewPostResult(event)
    }
}