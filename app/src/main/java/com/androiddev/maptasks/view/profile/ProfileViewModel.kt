package com.androiddev.maptasks.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl
import com.androiddev.maptasks.view.localDB.tables.CustomerListData

class ProfileViewModel(private val databaseHelper: DatabaseHelperImpl) : ViewModel() {
    fun getCustomerByMobileNumber(mobileNumber: String): LiveData<CustomerListData> {
        return databaseHelper.getCustomerByMobileNumber(mobileNumber)
    }
}

class ProfileViewModelFactory(private val databaseHelper: DatabaseHelperImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
