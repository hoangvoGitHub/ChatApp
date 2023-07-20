package com.hoangkotlin.chatapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hoangkotlin.chatapp.data.local.user.UserDao
import com.hoangkotlin.chatapp.data.local.channel.ChatChannel
import com.hoangkotlin.chatapp.data.local.channel.ChatChannelDao
import com.hoangkotlin.chatapp.data.local.membership.Membership
import com.hoangkotlin.chatapp.data.local.membership.MembershipDao
import com.hoangkotlin.chatapp.data.local.message.ChatMessage
import com.hoangkotlin.chatapp.data.local.message.ChatMessageDao
import com.hoangkotlin.chatapp.data.local.user.User

@Database(
    entities = [User::class, ChatChannel::class, ChatMessage::class, Membership::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatUserDao(): UserDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatChannelDao(): ChatChannelDao
    abstract fun membershipDao(): MembershipDao


    companion object {

        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            synchronized(AppDatabase::class.java) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
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



