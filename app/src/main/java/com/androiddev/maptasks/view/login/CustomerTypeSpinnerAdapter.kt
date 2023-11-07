package com.androiddev.maptasks.view.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.androiddev.maptasks.R
import com.androiddev.maptasks.model.CommonSpinner

class CustomerTypeSpinnerAdapter(context: Context, resource: Int, categoryList: List<CommonSpinner>):
    ArrayAdapter<CommonSpinner?>(context, resource, categoryList as List<CommonSpinner?>) {

    private val categoryList: List<CommonSpinner> = categoryList
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): CommonSpinner? {
        return categoryList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inf = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inf.inflate(R.layout.spinner_row, null)
        }
        val category = convertView!!.findViewById<View>(R.id.item_spinner) as TextView
        val layoutParams = category.layoutParams as LinearLayout.LayoutParams
        layoutParams.setMargins(8, 0, 0, 0)
        category.layoutParams = layoutParams
        category.setText(getItem(position)?.name)

//        if(position == 0) {
//            category.setTextColor(context.resources.getColor(R.color.color3))
//        }else{
//            category.setTextColor(context.resources.getColor(R.color.white))
//        }

//        if (position == 0) {
//            category.setTextColor(context.resources.getColor(R.color.referText))
//        }
        //AppController.hideProgressDialog(context);
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inf = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inf.inflate(R.layout.spinner_popup_row, null)
        }
        val category = convertView!!.findViewById<View>(R.id.item_spinner) as TextView
        category.setText(getItem(position)?.name)
        //AppController.hideProgressDialog(context);

        //Log.d("======DATA=======",this.getItem(position).getName());
        return convertView
    }
}