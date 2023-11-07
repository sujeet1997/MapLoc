package com.androiddev.maptasks.view.localDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androiddev.maptasks.view.localDB.tables.CustomerListData

@Dao
interface RoomApi {

    /*** Customer List ***/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data: CustomerListData): Long

    @get:Query("SELECT * FROM offline_customer_list_data ORDER BY customerName DESC")
    val allCustomerData: LiveData<List<CustomerListData>>

    @Query("SELECT * FROM offline_customer_list_data WHERE mobileNo = :username AND password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): LiveData<List<CustomerListData>>

    @Query("SELECT * FROM offline_customer_list_data WHERE mobileNo = :mobileNumber")
    fun getCustomerByMobileNumber(mobileNumber: String): LiveData<CustomerListData>


    @Query("DELETE FROM offline_customer_list_data")
    suspend fun deleteAllCustomerData()


}