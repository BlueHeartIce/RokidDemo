package com.blue.glassesapp.core.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.blankj.utilcode.util.LogUtils
import com.blue.glassesapp.core.db.dao.DaoMaster
import com.blue.glassesapp.core.db.dao.DaoMaster.OpenHelper
import org.greenrobot.greendao.database.Database

class MyOpenHelper(context: Context?, name: String?) : OpenHelper(context, name) {

    override fun onCreate(db: SQLiteDatabase?) {
        super.onCreate(db)
    }

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
        LogUtils.d("MyOpenHelperonUpgrade方法被调用", oldVersion, newVersion)
        MigrationHelper.migrate(
            db,
            object : MigrationHelper.ReCreateAllTableListener {
                override fun onCreateAllTables(db: Database?, ifNotExists: Boolean) {
                    DaoMaster.createAllTables(db, ifNotExists)
                }

                override fun onDropAllTables(db: Database?, ifExists: Boolean) {
                    DaoMaster.dropAllTables(db, ifExists)
                }
            },
        )
    }
}
