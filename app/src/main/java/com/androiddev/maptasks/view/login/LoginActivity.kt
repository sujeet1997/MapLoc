package com.androiddev.maptasks.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androiddev.maptasks.BuildConfig
import com.androiddev.maptasks.R
import com.androiddev.maptasks.databinding.ActivityLoginBinding
import com.androiddev.maptasks.model.CommonSpinner
import com.androiddev.maptasks.utils.BlockMultipleClick
import com.androiddev.maptasks.utils.PreferenceHelper
import com.androiddev.maptasks.view.dashboard.DashboardActivity
import com.androiddev.maptasks.view.localDB.DatabaseBuilder
import com.androiddev.maptasks.view.localDB.DatabaseHelperImpl
import com.androiddev.maptasks.view.map.MapActivity
import com.androiddev.maptasks.view.profile.ProfileActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var dbHelper: DatabaseHelperImpl

    var userTypeList = mutableListOf<CommonSpinner>()
    private lateinit var mSelecteduserType: CommonSpinner
    var userTypeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // local DB instance
        dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(this.applicationContext))
        // Create a ViewModelFactory with the required dependencies
        val viewModelFactory = LoginViewModelFactory(dbHelper)

        // Initialize the ViewModel using the factory
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.loginButton.setOnClickListener(this)
        binding.custTypeSpinner.onItemSelectedListener = this

        customerTypeSpinner()
        passView()
    }


    private fun customerTypeSpinner() {
        userTypeList.clear()

        userTypeList.add( CommonSpinner(name = "Select Customer Type", id = -1))
        userTypeList.add( CommonSpinner(name = "Parent", id = 1))
        userTypeList.add( CommonSpinner(name = "Child", id = 2))


        binding.custTypeSpinner.adapter = CustomerTypeSpinnerAdapter(this, R.layout.spinner_popup_row,userTypeList)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.login_button ->{
                if(BlockMultipleClick.click()) return
                if (userTypeId == -1){
                    Toast.makeText(this, getString(R.string.please_select_customer_type), Toast.LENGTH_SHORT).show()
                    return
                } else if (binding.userName.text.toString().isNullOrEmpty()){
                    binding.userName.error = "Enter Mobile"
                    binding.userName.requestFocus()
                }else if (binding.password.text.toString().isNullOrEmpty()){
                    binding.password.error = "Enter Password"
                    binding.password.requestFocus()
                }else if (!binding.password.text.toString().isNullOrEmpty() && binding.password.text.toString().length < 6){
                    binding.password.error = "Please enter valid password"
                    binding.password.requestFocus()
                } else{

                    if (userTypeId == 1){
                        if (binding.userName.text.toString().equals("parent12",true) && binding.password.text.toString() == "123456"){
                            PreferenceHelper.setBooleanValue(this, BuildConfig.IsLoggedIn, true)
                            PreferenceHelper.setStringValue(this, BuildConfig.CustomerType, userTypeId.toString())

                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }else{
                            Toast.makeText(this, getString(R.string.invalid_parent_credential), Toast.LENGTH_SHORT).show()

                        }

                    }else if (userTypeId == 2){
                        fetchUserDetailsFromDB()

                    }

                }

            }

        }


    }

    private fun fetchUserDetailsFromDB() {

        loginViewModel.loginUser(binding.userName.text.toString(), binding.password.text.toString()).observe(this, { loginSuccessful ->
            if (loginSuccessful) {

                PreferenceHelper.setBooleanValue(this, BuildConfig.IsLoggedIn, true)
                PreferenceHelper.setStringValue(this, BuildConfig.CustomerType, userTypeId.toString())
                PreferenceHelper.setStringValue(this, BuildConfig.UserName, binding.userName.text.toString())

                val intent = Intent(this, MapActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            } else {
                Toast.makeText(this, getString(R.string.incorrect_username_passw), Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when((parent as Spinner).id){
            R.id.cust_type_spinner -> {
                mSelecteduserType = parent.getItemAtPosition(position) as CommonSpinner
                userTypeId = mSelecteduserType.id!!
                Log.d("fdsfsdf", mSelecteduserType.name!!)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun passView() {
        binding.password.setOnTouchListener(View.OnTouchListener { view, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.password.right - binding.password.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    if (binding.password.transformationMethod == PasswordTransformationMethod.getInstance()){
                        binding.password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_view, 0)
                        binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }else{
                        binding.password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_passw, 0)
                        binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }
}