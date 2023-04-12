package com.hoangkotlin.chatapp.data.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hoangkotlin.chatapp.data.model.*
import com.hoangkotlin.chatapp.utils.MapConverter
import com.hoangkotlin.chatapp.utils.MemberConverter

@Database(
    entities = [User::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    MapConverter::class,
    MemberConverter::class

)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
//    abstract fun messageDao(): MessageDao
//    abstract fun channelDao(): ChannelDao


    companion object {

        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            synchronized(AppDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "my_database"
                    ).addMigrations(MIGRATION_1_2)
                        .addCallback(MyDatabaseCallBack()).build()
                }
            }
            return INSTANCE
        }

    }

}

class MyDatabaseCallBack : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS room_json USING json1")

    }

}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Rename the old column
        database.execSQL("ALTER TABLE $CHANNEL_ENTITY_TABLE_NAME RENAME COLUMN member TO members")

    }
}