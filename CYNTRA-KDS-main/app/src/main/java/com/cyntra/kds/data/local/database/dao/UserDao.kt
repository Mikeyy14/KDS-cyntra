package com.cyntra.kds.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("Select username from User order by id desc")
    suspend fun getAllUsersNames():List<String>

    @Query("Select username from User where id = :id")
    suspend fun getUserById(id:Int):String

    @Query("Insert into User values (NULL, :userName) ")
    suspend fun insertUser(userName:String)
}