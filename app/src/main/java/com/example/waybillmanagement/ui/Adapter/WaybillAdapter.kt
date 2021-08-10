package com.example.waybillmanagement.ui.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waybillmanagement.WaybillApplication
import com.example.waybillmanagement.logic.model.Waybill
import com.example.waybillmanagement.ui.AddOrderActivity
import com.example.waybillmanagement.ui.R

class WaybillAdapter(val context: Context, val waybillList: List<Waybill>?):
                    RecyclerView.Adapter<WaybillAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val route: TextView = view.findViewById(R.id.waybill_info_route)
        val goods: TextView = view.findViewById(R.id.waybill_info_goods)
        val waybillid: TextView = view.findViewById(R.id.waybill_info_id)
        val consignee: TextView = view.findViewById(R.id.waybill_info_consignee)
        val fee: TextView = view.findViewById(R.id.waybill_info_fee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.waybill_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waybill = waybillList?.get(position)
        holder.route.text = (waybill?.departure ?: "null" ) + "-" + (waybill?.arrival ?: "null")
        holder.goods.text = (waybill?.goodsname ?: "null") + " " + (waybill?.numberofPackages ?: "null")
        holder.waybillid.text = "NO: " + (waybill?.waybillno ?: "null")
        holder.consignee.text = "收货人: " + (waybill?.consignee ?: "null") + "(" + (waybill?.ceephone
                ?: "null") + ")"
        holder.fee.text = "到付" + (waybill?.rpaid ?: "null") + "元"

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            Log.d("item",position.toString())
            val curwaybill = waybillList?.get(position)
            val intent = Intent(context,AddOrderActivity::class.java).apply {
                putExtra("modify",true)
                putExtra("curwaybill",curwaybill)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = waybillList?.size ?: 0
}