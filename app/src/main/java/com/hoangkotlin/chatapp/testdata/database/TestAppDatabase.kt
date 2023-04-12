package com.hoangkotlin.chatapp.testdata.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hoangkotlin.chatapp.testdata.user.UserDao
import com.hoangkotlin.chatapp.testdata.channel.ChatChannel
import com.hoangkotlin.chatapp.testdata.channel.ChatChannelDao
import com.hoangkotlin.chatapp.testdata.membership.Membership
import com.hoangkotlin.chatapp.testdata.membership.MembershipDao
import com.hoangkotlin.chatapp.testdata.message.ChatMessage
import com.hoangkotlin.chatapp.testdata.message.ChatMessageDao
import com.hoangkotlin.chatapp.testdata.user.User

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
                    ).build()
                }
            }
            INSTANCE.openHelper.writableDatabase.execSQL("PRAGMA foreign_keys = OFF")
            return INSTANCE
        }
    }

}

//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE $USER_ENTITY_TABLE_NAME ADD COLUMN image TEXT DEFAULT NULL")
//    }
//}



