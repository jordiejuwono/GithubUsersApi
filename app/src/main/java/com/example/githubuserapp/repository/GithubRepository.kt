package com.example.githubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserapp.data.room.UsersDao
import com.example.githubuserapp.data.room.UsersDatabase
import com.example.githubuserapp.data.room.UsersEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GithubRepository(application: Application) {

    private val usersDao: UsersDao
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UsersDatabase.getDatabase(application)
        usersDao = db.usersDao()
    }

    fun getAllFavorites() : LiveData<List<UsersEntity>> = usersDao.getAllFavorites()

    fun insert(usersEntity: UsersEntity) {
        executor.execute {
            usersDao.insertToFavorites(usersEntity)
        }
    }

    fun delete(usersEntity: UsersEntity) {
        executor.execute {
            usersDao.deleteFromFavorites(usersEntity)
        }
    }

}