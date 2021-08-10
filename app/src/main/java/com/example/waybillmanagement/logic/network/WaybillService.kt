package com.example.waybillmanagement.logic.network

import com.example.waybillmanagement.logic.model.WaybillResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface WaybillService {

    /*请求服务器上的json数据*/
    @GET("simulated-Waybills-db.json")
    fun getjsondata(): Call<WaybillResponse>

    /*请求服务器上的xml数据*/
    @GET("simulated-Waybills-db.xml")
    fun getxmldata(): Call<WaybillResponse>

}