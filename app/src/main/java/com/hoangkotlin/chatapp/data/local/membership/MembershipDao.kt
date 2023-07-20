package com.hoangkotlin.chatapp.data.local.membership

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MembershipDao {
    @Query(
        "SELECT cid FROM $MEMBERSHIP_ENTITY_TABLE_NAME WHERE " +
                "uid IN (:userIds) GROUP BY cid HAVING COUNT(DISTINCT uid) =:numUsers"
    )
    fun getCidByUsers(userIds: List<String>, numUsers: Int = userIds.size): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(membership: Membership)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(memberships: List<Membership>)

}