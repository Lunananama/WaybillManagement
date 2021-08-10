package com.example.waybillmanagement.logic.network

import android.util.Log
import com.example.waybillmanagement.logic.model.Waybill
import com.example.waybillmanagement.logic.model.WaybillResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import java.net.URLDecoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WaybillNetwork {

    private val waybillJSONService = ServiceCreater.createJson<WaybillService>()

    private val waybillXMLService = ServiceCreater.createXML<WaybillService>()

    //调用WaybillService中请求JSON数据的方法
    suspend fun getjsondata() = waybillJSONService.getjsondata().await()

    //请求XML数据
    suspend fun getxmldata() = waybillXMLService.getxmldata().await()

    //挂起函数
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    Log.d("response", response.raw().toString())

                    if (body != null) {
                        continuation.resume(body)

                    } else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}

