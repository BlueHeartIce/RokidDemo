package com.blue.glassesapp.core.db

import android.content.Context
import com.blue.glassesapp.core.db.dao.DaoMaster
import com.blue.glassesapp.core.db.dao.DaoSession
import com.blue.glassesapp.core.db.dao.GlassesRecordModelDao
import com.blue.glassesapp.core.db.entity.GlassesRecordModel
import org.greenrobot.greendao.database.Database

object DBManager {
    private const val DB_NAME = "glassesapp-db"
    private var daoSession: DaoSession? = null
    private var database: Database? = null

    /**
     * 初始化数据库，只需在 Application 中调用一次
     */
    @Synchronized
    fun init(context: Context) {
        if (daoSession == null) {
            val helper = DaoMaster.DevOpenHelper(context, DB_NAME)
            database = helper.writableDb
            val daoMaster = DaoMaster(database)
            daoSession = daoMaster.newSession()
        }
    }


    /**
     * 获取 DaoSession
     */
    fun getDaoSession(): DaoSession {
        return daoSession ?: throw IllegalStateException("DBManager not initialized")
    }

    /**
     * 获取指定 Dao
     */
    fun <T> getDao(daoClass: Class<T>): T {
        return getDaoSession().javaClass.getMethod("get${daoClass.simpleName}")
            .invoke(getDaoSession()) as T
    }

    /**
     * 清空缓存
     */
    fun clear() {
        daoSession?.clear()
    }

    /**
     * 写入眼镜请求记录数据
     */
    fun writeGlassesRequestRecord(record: GlassesRecordModel) {
        getDao(GlassesRecordModelDao::class.java).insert(record)
    }

    /**
     * 关闭数据库
     */
    fun close() {
        daoSession?.clear()
        database?.close()
        daoSession = null
        database = null
    }


    /**
     * 分页查询眼镜请求记录数据
     */
    fun queryGlassesRequestRecord(page: Int, pageSize: Int): List<GlassesRecordModel> {
        return getDao(GlassesRecordModelDao::class.java).queryBuilder()
            .orderDesc(GlassesRecordModelDao.Properties.Timestamp,GlassesRecordModelDao.Properties.Id)
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .list()
    }
}