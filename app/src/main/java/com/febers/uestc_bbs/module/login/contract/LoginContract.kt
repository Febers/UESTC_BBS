package com.febers.uestc_bbs.module.login.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.UserSimpleBean

/**
 * Mvp模式中的契约类，在其中定义一个事务需要的Model、Presenter和View
 *
 * 根据接口和抽象类的区别，Model和View应该代表一种行为，即获取数据和显示数据
 * Presenter应该代表一个对象，这个对象联系上面两种行为
 *
 * Model获取和处理数据之后，将结果通过一个event对象(BaseEvent的实现类)传递给View
 *
 * Model一般只会将操作成功之后的数据传给Presenter，因为如果获取数据出错
 * 包括网络错误、IO错误跟服务器返回的错误，都会调用BasePresenter的errorResult方法
 * 然后errorResult方法内会调用BaseView的showError方法
 *
 * event对象包含code，状态码和data，数据，本项目基本上所有消息的传递都通过BaseEvent<T>进行
 *
 * module包内所有事务都采用这种架构，在不同的包内，如model包内，有具体的实现类XXModelImpl
 */
interface LoginContract {

    interface Model {
        /**
         * 登录服务
         * 本项目所有Mvp的Model提供的公共方法都以Service作为后缀
         *
         * @param userName 用户名称
         * @param userPw 用户密码
         */
        fun loginService(userName: String, userPw: String)
    }

    interface View: BaseView {
        /**
         * 登录的结果
         * 一般来说，只有在登录成功之后才调用此方法
         */
        fun showLoginResult(event: BaseEvent<UserSimpleBean>)
    }

    abstract class Presenter(view: View): BasePresenter<View>(mView = view) {
        /**
         * 登录请求，实现类内部应该调用新建一个具体的model，并调用其XXService方法
         * 他们的参数应该是一致的
         *
         * @param userName 用户名称
         * @param userPw 用户密码
         */
        abstract fun loginRequest(userName: String, userPw: String)

        /**
         * 登录的结果，实现类应该调用view.showXX
         *
         * @param event 传递消息的类
         */
        abstract fun loginResult(event: BaseEvent<UserSimpleBean>)
    }
}