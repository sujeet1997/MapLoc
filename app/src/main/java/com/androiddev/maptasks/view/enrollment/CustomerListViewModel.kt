package com.androiddev.maptasks.view.enrollment

import androidx.lifecycle.*
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl
import com.androiddev.maptasks.view.localDB.tables.CustomerListData
import kotlinx.coroutines.launch

class CustomerListViewModel(private val dbHelper: DatabaseHelperImpl) : ViewModel() {
    /*** Insert customer data to local DB ***/
    private val _customerListLiveData = MutableLiveData<CustomerListData>()
    val customerListLiveData: LiveData<CustomerListData> = _customerListLiveData

    fun insertCustomerData(customerData: CustomerListData) {
        viewModelScope.launch {
            try {
                if(dbHelper.insertData(customerData) > 0){
                    _customerListLiveData.value = customerData
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

class CustomerViewModelFactory(private val dbHelper: DatabaseHelperImpl) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomerListViewModel(dbHelper) as T
    }
}