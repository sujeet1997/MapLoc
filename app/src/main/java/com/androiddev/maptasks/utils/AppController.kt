package com.androiddev.maptasks.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.File
import java.io.FileOutputStream

object AppController {

    fun getAddress(context: Context, lat: Double, lng: Double): String? {
        val context = context
        val geocoder = Geocoder(context, Locale.getDefault())
        var add = ""
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if(addresses!!.isNotEmpty()) {
                val obj = addresses[0]
                add = obj.getAddressLine(0)

                Log.v("IGA", "Address$add")
            }
        } catch (e: IOException) {
            e.printStackTrace()
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Log.v("IGA", "Address" + e.message)
        }
        return add
    }




    /*fun convertLayoutToImage(context: Context, layoutResId: Int, width: Int, height: Int, filename: String) {
        // Inflate the XML layout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(layoutResId, null) as ViewGroup
        view.layoutParams = ViewGroup.LayoutParams(width, height)
        view.layout(0, 0, width, height)

        // Create a bitmap and a canvas to draw the layout
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw the layout on the canvas
        view.draw(canvas)

        // Save the bitmap as a PNG image
        val outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val outputFile = File(outputPath, "$filename.png")

        try {
            val outputStream = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    convertLayoutToImage(this, R.layout.your_layout, 600, 800, "layout_image")
*/

}