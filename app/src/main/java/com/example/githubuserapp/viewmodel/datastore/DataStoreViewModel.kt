package com.example.githubuserapp.viewmodel.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.data.datastore.SettingPreferences
import kotlinx.coroutines.launch

class DataStoreViewModel(private val pref: SettingPreferences) : ViewModel() {

    //setup for data store
    fun getThemeSettings() : LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}