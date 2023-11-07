package com.androiddev.maptasks.view.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androiddev.maptasks.databinding.RowCustomerBinding
import com.androiddev.maptasks.utils.BlockMultipleClick
import com.androiddev.maptasks.view.localDB.tables.CustomerListData

class CustomerListAdapter(private val listData: List<CustomerListData>, var onItemClickListener: OnItemDetailClickCallBack):RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {

    interface OnItemDetailClickCallBack {
        fun onListDetailsItemClickResponse(itemView: View, customerListData: CustomerListData, )
    }

    class ViewHolder (val binding: RowCustomerBinding): RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val mobile = binding.mobile
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowCustomerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]

        holder.name.text = data.customerName
        holder.mobile.text = data.mobileNo

        holder.itemView.setOnClickListener { v ->
            if (BlockMultipleClick.click()) return@setOnClickListener

            onItemClickListener.onListDetailsItemClickResponse(v, data)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }
}