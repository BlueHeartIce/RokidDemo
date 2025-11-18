package com.blue.glassesapp.core.db.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.io.IOException;

public class DataBaseContext extends ContextWrapper {
    public DataBaseContext(Context base) {
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则自动创建
     */
//    @Override
//    public File getDatabasePath(String name) {
//        //判断是否存在sd卡
//        String dbPath = FileUtil.INSTANCE.getDbFolder() + name;//数据库路径
//        FileUtil.INSTANCE.getDbFolder();
//        //数据库文件是否创建成功
//        //判断文件是否存在，不存在则创建该文件
//        File dbFile = new File(dbPath);
//        if (!dbFile.exists()) {
//            try {
//                dbFile.createNewFile();//创建文件
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //返回数据库文件对象
//        return dbFile;
//    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(
            String name, int mode,
            SQLiteDatabase.CursorFactory factory
    ) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }


    @Override
    public SQLiteDatabase openOrCreateDatabase(
            String name, int mode,
            SQLiteDatabase.CursorFactory factory,
            DatabaseErrorHandler errorHandler
    ) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}
