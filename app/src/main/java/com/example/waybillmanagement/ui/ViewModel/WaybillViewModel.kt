package com.example.waybillmanagement.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.waybillmanagement.logic.Repository
import com.example.waybillmanagement.logic.model.Waybill

class WaybillViewModel: ViewModel() {

    private val getLiveData = MutableLiveData<String>()

    val waybillList = ArrayList<Waybill>()

    var curname: String = ""
    var curpass: String = ""
    var cursex: String = ""
    var curdepart: String = ""
    var curwaybill = Waybill()
    var modify: Boolean = false
    var querytype: String = ""

    val waybillListLiveData = Transformations.switchMap(getLiveData) { query->
        Repository.getdata(query)
    }

    fun getdata(query: String){
        getLiveData.value = query
    }
}