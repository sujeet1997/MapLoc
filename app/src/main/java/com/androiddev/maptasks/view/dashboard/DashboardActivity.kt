package com.androiddev.maptasks.view.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.androiddev.maptasks.BuildConfig
import com.androiddev.maptasks.R
import com.androiddev.maptasks.databinding.ActivityDashboardBinding
import com.androiddev.maptasks.utils.PreferenceHelper
import com.androiddev.maptasks.view.dashboard.adapter.CustomerListAdapter
import com.androiddev.maptasks.view.enrollment.EnrollmentActivity
import com.androiddev.maptasks.view.localDB.DatabaseBuilder
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl
import com.androiddev.maptasks.view.localDB.tables.CustomerListData
import com.androiddev.maptasks.view.login.LoginActivity
import com.androiddev.maptasks.view.profile.ProfileActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity(), CustomerListAdapter.OnItemDetailClickCallBack, View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var dbHelper: DatabaseHelperImpl

    lateinit var logout: MenuItem
    lateinit var profile: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Dashboard"
        // local DB instance
        dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(this.applicationContext))

        binding.enrollCustomer.setOnClickListener(this)


        fetchCustomerFromLocal()

    }

    private fun fetchCustomerFromLocal() {
        dbHelper.allCustomerData.observe(this, Observer {
            if (it.isEmpty()){
                binding.noData.visibility = View.VISIBLE
                binding.customerRecycler.visibility = View.GONE
            }else{
                binding.customerRecycler.visibility = View.VISIBLE
                binding.noData.visibility = View.GONE
                binding.customerRecycler.adapter = CustomerListAdapter(it,this)

            }

        })

    }

    override fun onListDetailsItemClickResponse(
        itemView: View,
        customerListData: CustomerListData
    ) {

        PreferenceHelper.setStringValue(this, BuildConfig.UserName, customerListData.mobileNo.toString())
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.enroll_customer ->{
                startActivity(Intent(this, EnrollmentActivity::class.java))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        logout = menu.findItem(R.id.logout)
        profile = menu.findItem(R.id.profile)

        if (PreferenceHelper.getStringValue(this, BuildConfig.CustomerType) == BuildConfig.Parent){
            if (::profile.isInitialized){
                profile.isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.logout ->{
//                GlobalScope.launch {
//                    dbHelper.deleteAllCustomerData()
//                }
                PreferenceHelper.clear(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}