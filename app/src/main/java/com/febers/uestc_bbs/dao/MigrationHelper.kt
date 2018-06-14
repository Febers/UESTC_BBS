/*
 * Created by Febers at 18-6-15 上午12:17.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-15 上午12:17.
 */

package com.febers.uestc_bbs.dao

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.database.StandardDatabase
import org.greenrobot.greendao.internal.DaoConfig

import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Arrays

/**
 * https://www.jianshu.com/p/0db7e30e39ed
 */
object MigrationHelper {
    var DEBUG = false
    private val TAG = "MigrationHelper"
    private val SQLITE_MASTER = "sqlite_master"
    private val SQLITE_TEMP_MASTER = "sqlite_temp_master"

    private var weakListener: WeakReference<ReCreateAllTableListener>? = null

    interface ReCreateAllTableListener {
        fun onCreateAllTables(db: Database, ifNotExists: Boolean)

        fun onDropAllTables(db: Database, ifExists: Boolean)
    }

    fun migrate(db: SQLiteDatabase, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        printLog("【The Old Database Version】" + db.version)
        val database = StandardDatabase(db)
        migrate(database, *daoClasses)
    }

    fun migrate(db: SQLiteDatabase, listener: ReCreateAllTableListener, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        weakListener = WeakReference(listener)
        migrate(db, *daoClasses)
    }

    fun migrate(database: Database, listener: ReCreateAllTableListener, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        weakListener = WeakReference(listener)
        migrate(database, *daoClasses)
    }

    fun migrate(database: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        printLog("【Generate temp table】start")
        generateTempTables(database, *daoClasses)
        printLog("【Generate temp table】complete")

        var listener: ReCreateAllTableListener? = null
        if (weakListener != null) {
            listener = weakListener!!.get()
        }

        if (listener != null) {
            listener.onDropAllTables(database, true)
            printLog("【Drop all table by listener】")
            listener.onCreateAllTables(database, false)
            printLog("【Create all table by listener】")
        } else {
            dropAllTables(database, true, *daoClasses)
            createAllTables(database, false, *daoClasses)
        }
        printLog("【Restore data】start")
        restoreData(database, *daoClasses)
        printLog("【Restore data】complete")
    }

    private fun generateTempTables(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        for (i in daoClasses.indices) {
            var tempTableName: String? = null

            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            if (!isTableExists(db, false, tableName)) {
                printLog("【New Table】$tableName")
                continue
            }
            try {
                tempTableName = daoConfig.tablename + "_TEMP"
                val dropTableStringBuilder = StringBuilder()
                dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";")
                db.execSQL(dropTableStringBuilder.toString())

                val insertTableStringBuilder = StringBuilder()
                insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName)
                insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";")
                db.execSQL(insertTableStringBuilder.toString())
                printLog("【Table】" + tableName + "\n ---Columns-->" + getColumnsStr(daoConfig))
                printLog("【Generate temp table】$tempTableName")
            } catch (e: SQLException) {
                Log.e(TAG, "【Failed to generate temp table】" + tempTableName!!, e)
            }

        }
    }

    private fun isTableExists(db: Database?, isTemp: Boolean, tableName: String): Boolean {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false
        }
        val dbName = if (isTemp) SQLITE_TEMP_MASTER else SQLITE_MASTER
        val sql = "SELECT COUNT(*) FROM $dbName WHERE type = ? AND name = ?"
        var cursor: Cursor? = null
        var count = 0
        try {
            cursor = db.rawQuery(sql, arrayOf("table", tableName))
            if (cursor == null || !cursor.moveToFirst()) {
                return false
            }
            count = cursor.getInt(0)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return count > 0
    }


    private fun getColumnsStr(daoConfig: DaoConfig?): String {
        if (daoConfig == null) {
            return "no columns"
        }
        val builder = StringBuilder()
        for (i in daoConfig.allColumns.indices) {
            builder.append(daoConfig.allColumns[i])
            builder.append(",")
        }
        if (builder.length > 0) {
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }


    private fun dropAllTables(db: Database, ifExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        reflectMethod(db, "dropTable", ifExists, *daoClasses)
        printLog("【Drop all table by reflect】")
    }

    private fun createAllTables(db: Database, ifNotExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        reflectMethod(db, "createTable", ifNotExists, *daoClasses)
        printLog("【Create all table by reflect】")
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private fun reflectMethod(db: Database, methodName: String, isExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        if (daoClasses.size < 1) {
            return
        }
        try {
            for (cls in daoClasses) {
                val method = cls.getDeclaredMethod(methodName, Database::class.java, Boolean::class.javaPrimitiveType)
                method.invoke(null, db, isExists)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    private fun restoreData(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        for (i in daoClasses.indices) {
            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            val tempTableName = daoConfig.tablename + "_TEMP"

            if (!isTableExists(db, true, tempTableName)) {
                continue
            }

            try {
                // get all columns from tempTable, take careful to use the columns list
                val columns = getColumns(db, tempTableName)
                val properties = ArrayList<String>(columns.size)
                for (j in daoConfig.properties.indices) {
                    val columnName = daoConfig.properties[j].columnName
                    if (columns.contains(columnName)) {
                        properties.add("`$columnName`")
                    }
                }
                if (properties.size > 0) {
                    val columnSQL = TextUtils.join(",", properties)

                    val insertTableStringBuilder = StringBuilder()
                    insertTableStringBuilder.append("REPLACE INTO ").append(tableName).append(" (")
                    insertTableStringBuilder.append(columnSQL)
                    insertTableStringBuilder.append(") SELECT ")
                    insertTableStringBuilder.append(columnSQL)
                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";")
                    db.execSQL(insertTableStringBuilder.toString())
                    printLog("【Restore data】 to $tableName")
                }
                val dropTableStringBuilder = StringBuilder()
                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName)
                db.execSQL(dropTableStringBuilder.toString())
                printLog("【Drop temp table】$tempTableName")
            } catch (e: SQLException) {
                Log.e(TAG, "【Failed to restore data from temp table 】$tempTableName", e)
            }

        }
    }

    private fun getColumns(db: Database, tableName: String): List<String> {
        var columns: List<String>? = null
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName limit 0", null)
            if (null != cursor && cursor.columnCount > 0) {
                columns = Arrays.asList(*cursor.columnNames)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null)
                cursor.close()
            if (null == columns)
                columns = ArrayList()
        }
        return columns
    }

    private fun printLog(info: String) {
        if (DEBUG) {
            Log.d(TAG, info)
        }
    }
}

