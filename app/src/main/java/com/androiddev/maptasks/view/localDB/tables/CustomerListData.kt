package com.androiddev.maptasks.view.localDB.tables

import androidx.annotation.Nullable
import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(tableName = "offline_customer_list_data", indices = [Index(value = ["mobileNo"], unique = true)])
data class CustomerListData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "mobileNo") var mobileNo: String? = null,
    @ColumnInfo(name = "customerName") var customerName: String? = null,
    @ColumnInfo(name = "password") var password: String? = null,
    @ColumnInfo(name = "dateOfBirth") var dateOfBirth: String? = null
):Serializable