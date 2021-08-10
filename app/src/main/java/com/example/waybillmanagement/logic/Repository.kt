package com.example.waybillmanagement.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.login.Database.AppDatabase
import com.example.waybillmanagement.WaybillApplication.Companion.context
import com.example.waybillmanagement.logic.model.Waybill
import com.example.waybillmanagement.logic.model.WaybillResponse
import com.example.waybillmanagement.logic.network.WaybillNetwork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.RuntimeException
import kotlin.concurrent.thread

object Repository {

    //请求数据
    fun getdata(query: String) = liveData(Dispatchers.IO) { //Dispatchers.IO保证所有的代码运行在子线程中
        val result = try {
            var waybillResponse: WaybillResponse

            if(query.equals("json")){   //JSON格式
                waybillResponse = WaybillNetwork.getjsondata()
            }
            else if (query.equals("xml")){  //XML格式
                waybillResponse = WaybillNetwork.getxmldata()
            }
            else{   //数据库
                val waybillDao = AppDatabase.getDatabase(context).waybillDao()
                val waybillList: List<Waybill> = waybillDao.loadAllWaybill()
                Log.d("database",waybillList.toString())
                waybillResponse = WaybillResponse(waybillList)
            }

            if (waybillResponse != null){
                val waybills = waybillResponse.waybillRecord
                Result.success(waybills)
            } else {
                Result.failure(RuntimeException("response json data is null"))
            }

        }catch (e: Exception){
            Result.failure<List<Waybill>>(e)
        }
        emit(result)
    }

}