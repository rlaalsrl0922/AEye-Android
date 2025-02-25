package com.example.aeye.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aeye.model.ObjectInfo
import kotlinx.coroutines.CoroutineScope

@Database(entities = [ObjectInfo::class], version = 1, exportSchema = false)
abstract class ObjectInfoDatabase : RoomDatabase() {
    abstract fun objectInfoDao(): ObjectInfoDao

    private class ObjectInfoDatabaseCallback(
        private val context: Context,
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let {
                initData(context.applicationContext, scope)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ObjectInfoDatabase? = null

        @Synchronized
        fun getDataBase(context: Context, scope: CoroutineScope): ObjectInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ObjectInfoDatabase::class.java,
                    "object-info-database"
                ).addCallback(ObjectInfoDatabaseCallback(context, scope))
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
            /**
            if (instance == null){
            synchronized(this){
            instance = Room.databaseBuilder(
            context.applicationContext,
            ObjectInfoDatabase::class.java,
            "object-info-database"
            ).addCallback(ObjectInfoDatabaseCallback(scope))
            .build()
            }
            }
            return instance
             */
        }
    }
}