package com.hoangkotlin.chatapp.utils

import androidx.room.TypeConverter
import com.hoangkotlin.chatapp.data.model.MemberEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class MapConverter {
    @OptIn(ExperimentalStdlibApi::class)
    private val memberEntityMapAdapter = moshi.adapter<Map<String, MemberEntity>>()

    @TypeConverter
    fun memberMapToString(someObjects: Map<String, MemberEntity>?):String?{
        return memberEntityMapAdapter.toJson(someObjects)
    }

    @TypeConverter
    fun stringToMemberMap(data: String?): Map<String, MemberEntity>? {
        if (data.isNullOrEmpty() || data == "null") {
            return emptyMap()
        }
        return memberEntityMapAdapter.fromJson(data)
    }
}

@OptIn(ExperimentalStdlibApi::class)
val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
    .build()