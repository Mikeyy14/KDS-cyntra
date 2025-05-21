package com.cyntra.kds.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo(name = "userName", defaultValue = "") var userName:String
    )

