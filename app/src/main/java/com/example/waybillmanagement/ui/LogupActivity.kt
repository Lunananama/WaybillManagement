package com.example.waybillmanagement.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.login.Dao.UserDao
import com.example.login.Database.AppDatabase
import com.example.waybillmanagement.logic.model.UserEnity
import com.example.waybillmanagement.WaybillApplication
import com.example.waybillmanagement.ui.ViewModel.LogupViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_logup.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

class LogupActivity : BaseActivity() {

    val viewModel = LogupViewModel()
    lateinit var sp: SharedPreferences
    lateinit var userDao: UserDao

    val inserUsersuccess = 1
    val inserUserfail = 2
    val telExisted = 3
    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message){
            when(msg.what){
                inserUsersuccess -> {
                    logupusernameEdit.text.clear()
                    logupuserpassEdit.text.clear()
                    logupusertelEdit.text.clear()
                    logupusersexSpinner.setSelection(0)
                    logupuserdepartSpinner.setSelection(0)
                    Toast.makeText(WaybillApplication.context,viewModel.name + "注册成功",Toast.LENGTH_SHORT).show()
                }
                inserUserfail -> {
                    Toast.makeText(WaybillApplication.context,"注册失败，出现未知错误",Toast.LENGTH_SHORT).show()
                }
                telExisted -> {
                    Toast.makeText(WaybillApplication.context,"该手机号已被注册",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logup)
        setSupportActionBar(loguptoolbar)

        supportActionBar?.setTitle("用户注册")

        sp = getPreferences(Context.MODE_PRIVATE)
        viewModel.name = sp.getString("nameReserved","").toString()
        viewModel.pass = sp.getString("passReserved","").toString()
        viewModel.depart = sp.getString("departReserved","").toString()
        viewModel.tel = sp.getString("tel","").toString()
        viewModel.sex = sp.getString("sex","").toString()

        viewModel.curname = intent.getStringExtra("curname").toString()
        viewModel.curpass = intent.getStringExtra("curpass").toString()
        userDao = AppDatabase.getDatabase(this).userDao()

        //性别
        logupusersexSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                viewModel.sex = resources.getStringArray(R.array.sexList).get(position).toString()
                Log.d("sex",viewModel.sex)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        //发货站
        logupuserdepartSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                viewModel.depart = resources.getStringArray(R.array.stationList).get(position).toString()
                Log.d("depart",viewModel.depart)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        //注册
        logupButton.setOnClickListener {
            viewModel.name = logupusernameEdit.text.toString()
            viewModel.pass = logupuserpassEdit.text.toString()
            viewModel.tel = logupusertelEdit.text.toString()

            if (viewModel.name.isEmpty()){
                Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show()
            }
            else if (viewModel.pass.isEmpty()){
                Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show()
            }
            else if (viewModel.pass.length > 8){
                Toast.makeText(this,"密码必须少于8位",Toast.LENGTH_SHORT).show()
            }
            else if (viewModel.depart.isEmpty()){
                Toast.makeText(this,"发货点不能为空",Toast.LENGTH_SHORT).show()
            }
            else if (viewModel.sex.isEmpty()){
                Toast.makeText(this,"性别不能为空",Toast.LENGTH_SHORT).show()
            }
            else if (!Pattern.compile("^1[3,5,7,8,9][0-9]{9}$").matcher(viewModel.tel).matches()){
                Toast.makeText(this,"请输入11位有效手机号",Toast.LENGTH_SHORT).show()
            }
            else{
                thread {
                    if (userDao.queryByTel(viewModel.tel).isNotEmpty()){
                        val msg = Message()
                        msg.what = telExisted
                        handler.sendMessage(msg)
                    }
                    else if (userDao.inserUser(UserEnity(viewModel.name,viewModel.pass,viewModel.depart,viewModel.tel,viewModel.sex))>0){
                        val msg = Message()
                        msg.what = inserUsersuccess
                        handler.sendMessage(msg)
                    }
                    else{
                        val msg = Message()
                        msg.what = inserUserfail
                        handler.sendMessage(msg)
                    }
                }
            }

        }

        //返回
        logupbackButton.setOnClickListener {
            this.finish()
            Log.d("logup","删掉我自己")
        }
    }

    //加载菜单布局
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    //菜单项的监听事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawyerlayout.openDrawer(GravityCompat.START) //打开滑动菜单
            R.id.userswitch -> {    //切换用户
                if (viewModel.curname.isNotEmpty()){
                    val intent = Intent(this,LoginActivity::class.java)
                    intent.putExtra("switchboolean",true)
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "当前未登录，无法切换用户", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.userregister -> {  //用户注册
                Toast.makeText(this, "当前已在注册页面", Toast.LENGTH_SHORT).show()
            }
            R.id.usersignout -> {   //用户退出
                if (viewModel.curname.isNotEmpty()){
                    viewModel.curname =""
                    viewModel.curpass =""
                    Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "当前未登录，无法退出", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

}
