package com.androiddev.maptasks.view.profile

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androiddev.maptasks.BuildConfig
import com.androiddev.maptasks.R
import com.androiddev.maptasks.databinding.ActivityProfileBinding
import com.androiddev.maptasks.utils.PreferenceHelper
import com.androiddev.maptasks.view.localDB.DatabaseBuilder
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl
import com.androiddev.maptasks.view.localDB.tables.CustomerListData
import com.androiddev.maptasks.view.map.MapActivity
import com.permissionx.guolindev.PermissionX

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var dbHelper: DatabaseHelperImpl


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // local DB instance
        dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(this.applicationContext))
        // Initialize the ViewModel
        val viewModelFactory = ProfileViewModelFactory(dbHelper)
        profileViewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)




        val bundle = intent
        if (bundle != null) {


            // Observe the LiveData returned by the ViewModel
            profileViewModel.getCustomerByMobileNumber(PreferenceHelper.getStringValue(this, BuildConfig.UserName)).observe(this, { customerData ->
                if (customerData != null) {

                    binding.nameTxt.text = customerData.customerName
                    binding.mobileTxt.text = customerData.mobileNo
                    binding.dobTxt.text = customerData.dateOfBirth
                    binding.passwordTxt.text = customerData.password
                }
            })

        }

        if (PreferenceHelper.getStringValue(this, BuildConfig.CustomerType) == BuildConfig.Parent){
            binding.mapView.visibility = View.VISIBLE
        }else{
            binding.mapView.visibility = View.GONE
        }

            binding.mapView.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.map_view ->{
                askPermission()

            }

        }
    }

    private fun askPermission() {

        PermissionX.init(this)
            .permissions(
//                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList,
                    "Allow permission to access the location to access Map",
                    "OK",
                    "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                    // Start Location access
//                    locationAccess()
                    startActivity(Intent(this, MapActivity::class.java))
                } else {
                    // when permission denied
                    onBackPressed()
                }
            }

    }


}