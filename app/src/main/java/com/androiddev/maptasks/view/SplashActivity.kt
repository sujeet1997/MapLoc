package com.androiddev.maptasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.androiddev.maptasks.BuildConfig
import com.androiddev.maptasks.R
import com.androiddev.maptasks.utils.PreferenceHelper
import com.androiddev.maptasks.view.dashboard.DashboardActivity
import com.androiddev.maptasks.view.login.LoginActivity
import com.androiddev.maptasks.view.map.MapActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            when {
                PreferenceHelper.getBooleanValue(this, BuildConfig.IsLoggedIn) -> {
                    if (PreferenceHelper.getStringValue(this, BuildConfig.CustomerType) == BuildConfig.Parent){
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }else if (PreferenceHelper.getStringValue(this, BuildConfig.CustomerType) == BuildConfig.Child){
                        val intent = Intent(this, MapActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                }
                else -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }

            finish()
        }, 2000)

    }


}