package com.example.login.Dao

import androidx.room.*
import com.example.waybillmanagement.logic.model.Waybill

@Dao
interface WaybillDao { //封装对数据库的操作，必须是接口

    @Insert
    fun insertWaybill(waybill: Waybill): Long//添加运单,插入完成后返回主键waybillno

    @Insert
    fun insertWaybills(waybills: List<Waybill>)

    @Update
    fun updateWaybill(waybill: Waybill) //修改运单

    @Query("select * from t_waybill") //查询所有运单
    fun loadAllWaybill(): List<Waybill>

    @Query("select * from t_waybill where consignor =:consignor") //按发货人查询货单
    fun loadAllWaybillConsignedBy(consignor: String): List<Waybill>

    @Query("select * from t_waybill where consignee =:consignee") //按收货人查询运单
    fun loadAllWaybillreceivedBy(consignee: String): List<Waybill>

    @Query("select * from t_waybill where waybillno =:waybillNo") //按货单id查询运单
    fun loadAllWaybillById(waybillNo: String): Waybill

    @Delete
    fun deleteWaybill(waybillEntity: Waybill) //删除运单

    @Query("delete from t_waybill where waybillno =:waybillno") //发件人和收件人删除运单，使用非实体类参数来增删改数据
    fun deleteWaybillByWaybillno(waybillno: Int): Int


}