/*
 * Created by Febers at 18-6-15 上午12:13.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-15 上午12:13.
 */

package com.febers.uestc_bbs.dao

import android.content.Context
import org.greenrobot.greendao.database.Database
import java.util.*

/**
 * 避免数据库版本更新时
 * 删除原数据库
 */
class SQLOpenHelper(context: Context, name: String): DaoMaster.OpenHelper(context, name) {

    @Suppress("unchecked")
    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
        MigrationHelper.migrate(db!!, object : MigrationHelper.ReCreateAllTableListener {
            override fun onCreateAllTables(db: Database, ifNotExists: Boolean) {
                DaoMaster.createAllTables(db, ifNotExists)
            }

            override fun onDropAllTables(db: Database, ifExists: Boolean) {
                DaoMaster.dropAllTables(db, ifExists)
            }
        }, UserDao::class.java)
    }
}