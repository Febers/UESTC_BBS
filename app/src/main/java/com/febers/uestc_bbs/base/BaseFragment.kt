package com.febers.uestc_bbs.base


import android.os.Bundle
import androidx.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.custom.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.runOnUiThread

const val FID = "mFid"
const val UID = "mUid"
const val TITLE = "title"
const val MSG_TYPE = "mMsgType"
const val POST_TITLE = "post_title"

/**
 * Fragment的基类
 *
 */
abstract class BaseFragment : SupportFragment(), BaseView {

    protected var mMsgType: String? = MSG_TYPE_REPLY
    protected var mTitle: String? = "i河畔"
    protected var mFid: Int = 0
    protected var mUid: Int = 0

    protected open fun setMenu(): Int? = null

    protected open fun setToolbar(): Toolbar? = null

    protected open fun setTitle(): String? = null

    protected open fun registerEventBus(): Boolean = false

    protected open fun initView() {}

    protected abstract fun setView(): Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        arguments?.let {
            mFid = it.getInt(FID, 0)
            mUid = it.getInt(UID, 0)
            mTitle = it.getString(TITLE)
            mMsgType = it.getString(MSG_TYPE)
        }
    }

    override fun onStart() {
        super.onStart()
        if (registerEventBus()) {
            if(!EventBus.getDefault().isRegistered(this)) {
                log("fragment register ${this.toString()}")
                EventBus.getDefault().register(this)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(setView(), container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //添加toolbar点击返回
//        val activity: AppCompatActivity = activity as AppCompatActivity
//        activity.setSupportActionBar(setToolbar())
//        activity.supportActionBar?.apply {
//            title = ""
//            setDisplayHomeAsUpEnabled(true)
//        }
        setToolbar()?.setNavigationOnClickListener { pop() }
        setTitle()?.apply { setToolbar()?.title = this }
        //添加Menu
        if (setMenu() != null) {
            setHasOptionsMenu(true)
            setToolbar()?.inflateMenu(setMenu()!!)
        }
        initView()
    }

    protected open fun getEmptyViewForRecyclerView(recyclerView: RecyclerView): View =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.layout_server_null, recyclerView.parent as ViewGroup, false)

    override fun onDestroy() {
        mDelegate.onDestroy()
        hideSoftInput()
        super.onDestroy()
    }

    override fun onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            log("fragment unregister ${this.toString()}")
            EventBus.getDefault().unregister(this)
        }
        hideSoftInput()
        super.onStop()
    }

    override fun showHint(msg: String) {
        context?.runOnUiThread {
            HintUtils.show(activity, msg)
        }
    }
}

/*
 *
 *        ┌─┐       ┌─┐
 *   ┌──┘ ┴───────┘ ┴──┐
 *   │                 │
 *   │       ───       │
 *   │  ─┬┘       └┬─  │
 *   │                 │
 *   │       ─┴─       │
 *   │                 │
 *   └───┐         ┌───┘
 *       │         │
 *       │         │
 *       │         │
 *       │         └──────────────┐
 *       │                        │
 *       │                        ├─┐
 *       │                        ┌─┘
 *       │                        │
 *       └─┐  ┐  ┌───────┬──┐  ┌──┘
 *         │ ─┤ ─┤       │ ─┤ ─┤
 *         └──┴──┘       └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */