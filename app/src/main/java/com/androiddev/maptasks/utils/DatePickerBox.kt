package com.androiddev.maptasks.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import android.util.DisplayMetrics





object DatePickerBox {

    fun date(futureDateLock:Int, activity: Activity?, dates: (date: String) -> Unit) {
        val c = Calendar.getInstance(Locale.ENGLISH)
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val res: Resources = activity!!.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration

        conf.setLocale(Locale("EN".toLowerCase()));
        res.updateConfiguration(conf, dm);

        Locale.setDefault(Locale.ENGLISH)

        val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                var  dayOfMonths = "$dayOfMonth"
                var  monthOfYears = "${monthOfYear+1}"

                if(dayOfMonth<10) dayOfMonths = "0$dayOfMonth"
                if(monthOfYear+1<10) monthOfYears = "0${monthOfYear+1}"

                dates("$dayOfMonths/${monthOfYears}/$year")
            }, year, month, day
        )
        dpd.show()
        if (futureDateLock == 1)
        dpd.datePicker.maxDate = System.currentTimeMillis();

        if (futureDateLock == 5)
        dpd.datePicker.minDate = System.currentTimeMillis();

    }

    fun dateCompare(
        context: Context?,
        fromDate: String,
        toDate: String,
        Val: (status: Boolean) -> Unit
    ) {

        if (fromDate == null || fromDate == "") {
            Val(true)
            return
        }

        if (toDate == null || toDate == "") {
            Val(true)
            return
        }


        val datecompare = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)

        when {
            datecompare.parse(fromDate).after(datecompare.parse(toDate)) -> {
                Toast.makeText(context, "From Date should smaller than To date", Toast.LENGTH_SHORT)
                    .show()
                Val(false)
            }
            datecompare.parse(toDate).before(datecompare.parse(fromDate)) -> {
                Toast.makeText(context, "To Date should greater than From date", Toast.LENGTH_SHORT)
                    .show()
                Val(false)
            }
            else -> Val(true)
        }
    }

}