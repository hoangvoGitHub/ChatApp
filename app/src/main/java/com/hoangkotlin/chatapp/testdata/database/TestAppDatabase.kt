package com.hoangkotlin.chatapp.testdata.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.hoangkotlin.chatapp.data.database.MyDatabaseCallBack
import com.hoangkotlin.chatapp.data.model.User
import com.hoangkotlin.chatapp.data.model.UserDao
import com.hoangkotlin.chatapp.testdata.channel.ChatChannel
import com.hoangkotlin.chatapp.testdata.channel.ChatChannelDao
import com.hoangkotlin.chatapp.testdata.membership.Membership
import com.hoangkotlin.chatapp.testdata.membership.MembershipDao
import com.hoangkotlin.chatapp.testdata.message.ChatMessage
import com.hoangkotlin.chatapp.testdata.message.ChatMessageDao

@Database(
    entities = [User::class, ChatChannel::class, ChatMessage::class, Membership::class],
    version = 1,
    exportSchema = false
)
abstract class TestAppDatabase : RoomDatabase() {
    abstract fun chatUserDao(): UserDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatChannelDao(): ChatChannelDao
    abstract fun membershipDao(): MembershipDao



    companion object {

        private lateinit var INSTANCE: TestAppDatabase

        fun getDatabase(context: Context): TestAppDatabase {
            synchronized(TestAppDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TestAppDatabase::class.java,
                        "my_test_database"
                    ).addCallback(MyDatabaseCallBack()).build()
                }
            }
            INSTANCE.openHelper.writableDatabase.execSQL("PRAGMA foreign_keys = OFF")
            return INSTANCE
        }
    }

}
