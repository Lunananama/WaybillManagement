package com.example.waybillmanagement.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object ServiceCreater {

    private const val BASE_URL = "http://60.12.122.142:6080/"  //根路径

    private val retrofitJSON = Retrofit.Builder()   //针对JSON对象
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createJson(serviceClass: Class<T>): T = retrofitJSON.create(serviceClass)   //创建动态代理对象
    inline fun <reified T> createJson(): T = createJson(T::class.java)  //泛型实化功能

    private val retrofitXML = Retrofit.Builder()    //针对XML对象
        .baseUrl(BASE_URL)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    fun <T> createXML(serviceClass: Class<T>): T = retrofitXML.create(serviceClass)   //创建动态代理对象
    inline fun <reified T> createXML(): T = createXML(T::class.java)  //泛型实化功能
}