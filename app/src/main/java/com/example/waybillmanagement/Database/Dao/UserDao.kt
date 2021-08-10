package com.example.login.Dao

import androidx.room.*
import com.example.waybillmanagement.logic.model.UserEnity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserUser(user: UserEnity): Long  //重复插入，就替换

    @Update
    fun updateUser(newUser: UserEnity)

    @Query("select * from t_user")
    fun queryAll(): List<UserEnity>

    @Query("select * from t_user where user_name=:name and user_pass=:pass")
    fun queryByNameandPass(name: String, pass: String): UserEnity

    @Query("select * from t_user where user_name=:name")
    fun queryByName(name: String): UserEnity

    @Query("select * from t_user where user_tel=:tel")
    fun queryByTel(tel: String): List<UserEnity>

    @Query("delete from t_user where user_name=:name and user_pass=:pass")
    fun deleteUser(name: String, pass: String)

}