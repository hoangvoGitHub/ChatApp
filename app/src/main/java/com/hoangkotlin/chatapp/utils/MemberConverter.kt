package com.hoangkotlin.chatapp.utils

import com.hoangkotlin.chatapp.data.model.MemberEntity
import androidx.room.TypeConverter
import com.squareup.moshi.adapter


class MemberConverter {
    @OptIn(ExperimentalStdlibApi::class)
    private val memberEntityMapAdapter = moshi.adapter<MemberEntity>()

    @TypeConverter
    fun memberToString(member: MemberEntity?): String? {
        return memberEntityMapAdapter.toJson(member)
    }

    @TypeConverter
    fun stringToMemberMap(data: String?): MemberEntity? {
        if (data.isNullOrEmpty() || data == "null") {
            return null
        }
        return memberEntityMapAdapter.fromJson(data)
    }
}