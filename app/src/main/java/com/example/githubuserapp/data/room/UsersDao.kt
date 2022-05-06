package com.example.githubuserapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToFavorites(usersEntity: UsersEntity)

    @Delete
    fun deleteFromFavorites(usersEntity: UsersEntity)

    @Query("SELECT * FROM users_table")
    fun getAllFavorites(): LiveData<List<UsersEntity>>

}