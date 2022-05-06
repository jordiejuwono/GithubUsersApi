package com.example.githubuserapp.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users_table")
@Parcelize
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    var login: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "image")
    var image: String
) : Parcelable