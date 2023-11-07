package com.androiddev.maptasks.view.enrollment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androiddev.maptasks.R
import com.androiddev.maptasks.databinding.ActivityEnrollmentBinding
import com.androiddev.maptasks.utils.DatePickerBox
import com.androiddev.maptasks.view.localDB.DatabaseBuilder
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl
import com.androiddev.maptasks.view.localDB.tables.CustomerListData
import java.util.*

class EnrollmentActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEnrollmentBinding
    private lateinit var customerListViewModel: CustomerListViewModel
    private lateinit var dbHelper: DatabaseHelperImpl

    var birthdate = ""

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Enrollment"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // local DB instance
        dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
        // viewModel provider
        customerListViewModel = ViewModelProvider(this, CustomerViewModelFactory(dbHelper)).get(CustomerListViewModel::class.java)



        binding.dob.setOnClickListener(this)
        binding.submitEnrollment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.dob ->{
                DatePickerBox.date(1, this) {
                    binding.dob.text = it.toString()
                    birthdate = it
                }
            }

            R.id.submit_enrollment ->{

                if (binding.mobileNumberEdt.text.toString().trim().isNullOrEmpty()){
                    binding.mobileNumberEdt.error = getString(R.string.enter_mobile_number)
                    binding.mobileNumberEdt.requestFocus()

                }else if (!binding.mobileNumberEdt.text.toString().trim().isNullOrEmpty() && binding.mobileNumberEdt.text.toString().trim().length < 10){
                    binding.mobileNumberEdt.error = getString(R.string.enter_valid_mobile_no)
                    binding.mobileNumberEdt.requestFocus()

                }else if (binding.nameEdt.text.toString().trim().isNullOrEmpty()){
                    binding.nameEdt.error = getString(R.string.enter_name)
                    binding.nameEdt.requestFocus()

                }else if (birthdate.isNullOrEmpty()){
                    Toast.makeText(this, getString(R.string.select_dob), Toast.LENGTH_SHORT).show()

                }else if (binding.passwordEdt.text.toString().trim().isNullOrEmpty()){
                    binding.passwordEdt.error = getString(R.string.enter_password)
                    binding.passwordEdt.requestFocus()

                }else if (binding.passwordEdt.text.toString().trim().length < 6){
                    binding.passwordEdt.error = getString(R.string.please_enter_6_digit_password)
                    binding.passwordEdt.requestFocus()

                }else if (binding.passwordReEnter.text.toString().trim().isNullOrEmpty()){
                    binding.passwordReEnter.error = getString(R.string.re_enter_password)
                    binding.passwordReEnter.requestFocus()

                }else if (binding.passwordReEnter.text.toString() != binding.passwordEdt.text.toString()) {
                    binding.passwordReEnter.error = getString(R.string.password_mismatch)
                    binding.passwordReEnter.requestFocus()
                }else{
                    /*** Inserting data to local DB ***/
                    customerListViewModel.insertCustomerData(
                        CustomerListData(
                            mobileNo = binding.mobileNumberEdt.text.toString(),
                            customerName = binding.nameEdt.text.toString(),
                            dateOfBirth = binding.dob.text.toString(),
                            password = binding.passwordEdt.text.toString(),
                    ))

                    localDbObserver()
                }

            }


        }
    }

    private fun localDbObserver() {
        /*** Customer Data insert observer ***/
        customerListViewModel.customerListLiveData.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(this, "Customer created successfully!", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        })


    }

}