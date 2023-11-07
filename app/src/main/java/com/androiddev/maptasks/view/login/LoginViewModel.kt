package com.androiddev.maptasks.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl

class LoginViewModel(private val databaseHelper: DatabaseHelperImpl) : ViewModel() {

    fun loginUser(username: String, password: String): LiveData<Boolean> {
        val loginResult = MutableLiveData<Boolean>()
        databaseHelper.getUserByUsernameAndPassword(username, password).observeForever { users ->
            if (users.isNotEmpty()) {
                loginResult.value = true // User authenticated
            } else {
                loginResult.value = false // Authentication failed
            }
        }
        return loginResult
    }
}

class LoginViewModelFactory(private val databaseHelper: DatabaseHelperImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}