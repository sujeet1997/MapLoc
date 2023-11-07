package com.androiddev.maptasks.view.localDB

import androidx.lifecycle.LiveData
import com.androiddev.maptasks.view.localDB.tables.CustomerListData

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : RoomApi {

    /*** Customer Lists ***/
    override suspend fun insertData(data: CustomerListData): Long = appDatabase.dao!!.insertData(data)

    override val allCustomerData: LiveData<List<CustomerListData>> get() = appDatabase.dao!!.allCustomerData

    override fun getUserByUsernameAndPassword(username: String, password: String): LiveData<List<CustomerListData>> {
        return appDatabase.dao!!.getUserByUsernameAndPassword(username, password)
    }

    override fun getCustomerByMobileNumber(mobileNumber: String): LiveData<CustomerListData> {
        return appDatabase.dao!!.getCustomerByMobileNumber(mobileNumber)
    }

    override suspend fun deleteAllCustomerData()  = appDatabase.dao!!.deleteAllCustomerData()


}