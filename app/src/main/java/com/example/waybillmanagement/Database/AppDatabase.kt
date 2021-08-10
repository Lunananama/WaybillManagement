package com.example.login.Database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.login.Dao.UserDao
import com.example.login.Dao.WaybillDao
import com.example.waybillmanagement.logic.model.UserEnity
import com.example.waybillmanagement.logic.model.Waybill

//版本号，包含哪些实体类，实体类之间逗号隔开
@Database(version = 2, entities = [UserEnity::class, Waybill::class], exportSchema = false)

//一定要是抽象类
abstract class AppDatabase: RoomDatabase() {

    //一定要提供抽象方法，获取之前的Dao实例
    abstract fun userDao(): UserDao
    abstract fun waybillDao(): WaybillDao

    //全局只存在一份数据库实例
    companion object {
        private var instance: AppDatabase? = null

        val MIGRATION_2_3 = object: Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE t_user " +
                                "ADD COLUMN user_memps CHAR DEFAULT n")
            }
        }

        val MIGRATION_3_4 = object: Migration(3,4){
            override fun migrate(database: SupportSQLiteDatabase) {
                //啥子都不做
            }
        }

          //数据库升级
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase){

                database.execSQL("CREATE TABLE t_waybill (" +
                        "    waybillno        CHAR (8)  PRIMARY KEY" +
                        "                                NOT NULL" +
                        "                                 UNIQUE," +
                        "    consignor        VARCHAR   ," +
                        "    cerphone         CHAR (11) ," +
                        "    consignee        VARCHAR   ," +
                        "    ceephone         CHAR (11) ," +
                        "    departure        VARCHAR   NOT NULL," +
                        "    arrival          VARCHAR   NOT NULL," +
                        "    address          TEXT      ," +
                        "    goodsname        VARCHAR   NOT NULL," +
                        "    numberofPackages INTEGER   NOT NULL," +
                        "    rpaid            INTEGER   ," +
                        "    cpaid            INTEGER   )")

                database.execSQL("CREATE TABLE t_user (" +
                        "    user_name   STRING    NOT NULL" +
                        "                          DEFAULT tempuser" +
                        "                          UNIQUE," +
                        "    user_pass   STRING    NOT NULL" +
                        "                          DEFAULT temppass," +
                        "    user_depart STRING    NOT NULL" +
                        "                          DEFAULT 未知," +
                        "    user_tel    CHAR (11) NOT NULL," +
                        "    user_sex    CHAR      DEFAULT 男," +
                        "    user_memps  CHAR DEFAULT n," +
                        "    user_ID     INTEGER   PRIMARY KEY AUTOINCREMENT" +
                        "                          NOT NULL" +
                        "                          UNIQUE)")

                Log.d("database","two tables are created")
            }
        }

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            //第一个参数一定要这么写，不然容易出现内存泄漏
            //第二个参数是数据库的Class类型
            //第三个参数是数据库名
            return Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "app_database.db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .apply {
                        instance = this
                    }
        }
    }
}