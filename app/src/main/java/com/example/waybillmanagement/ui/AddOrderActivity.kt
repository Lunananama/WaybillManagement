package com.example.waybillmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.login.Dao.WaybillDao
import com.example.login.Database.AppDatabase
import com.example.waybillmanagement.ActivityCollector
import com.example.waybillmanagement.WaybillApplication
import com.example.waybillmanagement.logic.model.Waybill
import com.example.waybillmanagement.ui.ViewModel.WaybillViewModel
import kotlinx.android.synthetic.main.activity_add_order.*
import kotlinx.android.synthetic.main.activity_logup.*
import kotlinx.android.synthetic.main.nav_header.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

class AddOrderActivity : BaseActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WaybillViewModel::class.java)}
    lateinit var waybillDao: WaybillDao

    val altersuccess = 1
    val alterfailure = 2
    val savesuccess = 3
    val savefailure = 4
    val deletesuccess = 5
    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message){
            when(msg.what){
                altersuccess -> {
                    Toast.makeText(WaybillApplication.context,"修改成功",Toast.LENGTH_SHORT).show()
                }
                alterfailure -> {
                    Toast.makeText(WaybillApplication.context,"无权修改云端运单",Toast.LENGTH_SHORT).show()
                }
                savesuccess -> {
                    //清空输入
                    arrivalEditText.text.clear()
                    addressEditText.text.clear()
                    consignorEditText.text.clear()
                    cortelEditText.text.clear()
                    consigneeEditText.text.clear()
                    ceetelEditText.text.clear()
                    goodsnameEditText.text.clear()
                    goodsnumEditText.text.clear()
                    rpaidEditText.text.clear()
                    cpaidEditText.text.clear()
                    Toast.makeText(WaybillApplication.context,"运单保存成功",Toast.LENGTH_SHORT).show()
                }
                savefailure -> {
                    Toast.makeText(WaybillApplication.context,"运单保存失败",Toast.LENGTH_SHORT).show()
                }
                deletesuccess -> {
                    Toast.makeText(WaybillApplication.context,"运单已被删除",Toast.LENGTH_SHORT).show()
                    this@AddOrderActivity.finish()
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
        setSupportActionBar(addordertoolbar)

        viewModel.curname = intent.getStringExtra("curname").toString() //用户名
        viewModel.curpass = intent.getStringExtra("curpass").toString() //密码
        viewModel.cursex = intent.getStringExtra("cursex").toString()   //性别
        viewModel.curdepart = intent.getStringExtra("curdepart").toString() //发货地
        viewModel.modify = intent.getBooleanExtra("modify",false)   //修改运单标志

        waybillDao = AppDatabase.getDatabase(this).waybillDao()

        if (viewModel.modify){
            viewModel.curwaybill = intent.getParcelableExtra<Waybill>("curwaybill")!!   //当前订单
            supportActionBar?.setTitle("修改运单")
            addorder_save.setText("修 改")
            addorderdepartText.setText("发站: ${viewModel.curwaybill.departure}")
            arrivalEditText.setText(viewModel.curwaybill.arrival)
            addressEditText.setText(viewModel.curwaybill.address)
            consignorEditText.setText(viewModel.curwaybill.consignor)
            cortelEditText.setText(viewModel.curwaybill.cerphone)
            consigneeEditText.setText(viewModel.curwaybill.consignee)
            ceetelEditText.setText(viewModel.curwaybill.ceephone)
            goodsnameEditText.setText(viewModel.curwaybill.goodsname)
            goodsnumEditText.setText(viewModel.curwaybill.numberofPackages)
            cpaidEditText.setText(viewModel.curwaybill.cpaid)
            rpaidEditText.setText(viewModel.curwaybill.rpaid)

            arrivalEditText.isEnabled = false
            addressEditText.isEnabled = false
            consignorEditText.isEnabled = false
            cortelEditText.isEnabled = false
            consigneeEditText.isEnabled = false
            ceetelEditText.isEnabled = false
            goodsnameEditText.isEnabled = false
            goodsnumEditText.isEnabled = false
            cpaidEditText.isEnabled = false
            rpaidEditText.isEnabled = false
        }
        else{
            addorderdepartText.setText("发站: ${viewModel.curdepart}")
            supportActionBar?.setTitle("添加运单")
            addorderalterButton.isVisible = false
            addorderdeleteButton.isVisible = false
        }

        //信息项可被编辑
        addorderalterButton.setOnClickListener {
            if (arrivalEditText.isEnabled == false){
                arrivalEditText.isEnabled = true
                addressEditText.isEnabled = true
                consignorEditText.isEnabled = true
                cortelEditText.isEnabled = true
                consigneeEditText.isEnabled = true
                ceetelEditText.isEnabled = true
                goodsnameEditText.isEnabled = true
                goodsnumEditText.isEnabled = true
                cpaidEditText.isEnabled = true
                rpaidEditText.isEnabled = true
            }
            else {

                arrivalEditText.isEnabled = false
                addressEditText.isEnabled = false
                consignorEditText.isEnabled = false
                cortelEditText.isEnabled = false
                consigneeEditText.isEnabled = false
                ceetelEditText.isEnabled = false
                goodsnameEditText.isEnabled = false
                goodsnumEditText.isEnabled = false
                cpaidEditText.isEnabled = false
                rpaidEditText.isEnabled = false
            }
        }

        //保存或修改
        addorder_save.setOnClickListener {

            val arrival = arrivalEditText.text.toString()
            val address = addressEditText.text.toString()
            val consignor = consignorEditText.text.toString()
            val cortel = cortelEditText.text.toString()
            val consignee = consigneeEditText.text.toString()
            val ceetel = ceetelEditText.text.toString()
            val goodsname = goodsnameEditText.text.toString()
            val goodsnum = goodsnumEditText.text.toString()
            val rpaid = rpaidEditText.text.toString()
            val cpaid = cpaidEditText.text.toString()

            var flag = 0    //手机号检查是否过关

            if (arrival.equals("")){
                Toast.makeText(this,"请输入到站",Toast.LENGTH_SHORT).show()
            }
            else if (goodsname.equals("")){
                Toast.makeText(this,"请输入货物名称",Toast.LENGTH_SHORT).show()
            }
            else if (goodsnum.equals("")){
                Toast.makeText(this,"请输入件数",Toast.LENGTH_SHORT).show()
            }
            else if (cortel.isNotEmpty()){
                if (!Pattern.compile("^1[3,5,7,8,9][0-9]{9}$").matcher(cortel).matches()){
                    flag = 1
                    Toast.makeText(this,"发货人：请输入11位有效手机号",Toast.LENGTH_SHORT).show()
                }
            }
            else if (ceetel.isNotEmpty()){
                if (!Pattern.compile("^1[3,5,7,8,9][0-9]{9}$").matcher(ceetel).matches()){
                    flag = 1
                    Toast.makeText(this,"收货人：请输入11位有效手机号",Toast.LENGTH_SHORT).show()
                }
            }

            if (flag == 0 &&!viewModel.modify){
                thread {
                    val newwaybill = Waybill("",consignor,cortel,consignee,ceetel,viewModel.curdepart,arrival,
                            address,goodsname,goodsnum,rpaid,cpaid)
                    var num = waybillDao.loadAllWaybill().size+2000040  //插入数据库返回的结果
                    newwaybill.waybillno = "L" + num
                    num = waybillDao.insertWaybill(newwaybill).toInt()

                    if (num > 0){
                        val msg = Message()
                        msg.what = savesuccess
                        handler.sendMessage(msg)
                    }
                    else{
                        val msg = Message()
                        msg.what = savefailure
                        handler.sendMessage(msg)
                    }
                }
            }
            else if (flag==0){
                thread {
                    if (waybillDao.loadAllWaybillById(viewModel.curwaybill.waybillno) != null){
                        val newwaybill = Waybill(viewModel.curwaybill.waybillno,consignor,cortel,consignee,ceetel,
                                viewModel.curdepart,arrival, address,goodsname,goodsnum,rpaid,cpaid)
                        waybillDao.updateWaybill(newwaybill)
                        val msg = Message()
                        msg.what = altersuccess
                        handler.sendMessage(msg)
                    }
                    else {
                        val msg = Message()
                        msg.what = alterfailure
                        handler.sendMessage(msg)
                    }
                }
            }

        }

        //删除
        addorderdeleteButton.setOnClickListener {
            thread {
                waybillDao.deleteWaybill(viewModel.curwaybill)
                val msg = Message()
                msg.what = deletesuccess
                handler.sendMessage(msg)
            }
        }

        //返回
        addorder_back.setOnClickListener {
            this.finish()
        }

        //设置滑动菜单栏导航按钮
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)          //让导航按钮显示出来
            it.setHomeAsUpIndicator(R.drawable.ic_menu) //设置导航栏图标
        }

        //滑动菜单项监听器
        addordernavView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.navAddOrder -> {
                    Toast.makeText(this,"当前已在添加运单页面", Toast.LENGTH_SHORT).show()
                }

                R.id.navJSONOrder -> {
                    Toast.makeText(this,"jsonorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    intent.putExtra("type","json")
                    startActivity(intent)
                    Log.d("MainActivity","JSONorder is clicked")
                }

                R.id.navXMLOrder -> {
                    Toast.makeText(this,"xmlorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    intent.putExtra("type","xml")
                    startActivity(intent)
                    Log.d("MainActivity","XMLorder is clicked")
                }

                R.id.navLOCOrder -> {
                    Toast.makeText(this,"locorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","local")
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("MainActivity","LOCorder is clicked")
                }
                R.id.navSetting -> {

                }
            }

            //监听菜单项选中事件，之后补充逻辑
            addorderdrawyerlayout.closeDrawers()
            true
        }

    }

    //加载菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (viewModel.cursex.equals("男")){
            headeruserImage.setImageResource(R.drawable.ic_maleuser)
        }
        else{
            headeruserImage.setImageResource(R.drawable.ic_femaleuser)
        }
        headerusernameText.setText("欢迎使用，${viewModel.curname}")
        headeruserdepartText.setText(viewModel.curdepart)

        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    //菜单项的监听事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> addorderdrawyerlayout.openDrawer(GravityCompat.START) //打开滑动菜单
            R.id.userswitch -> {    //切换用户
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("switchboolean",true)
                intent.putExtra("curname",viewModel.curname)
                intent.putExtra("curpass",viewModel.curpass)
                startActivity(intent)
            }
            R.id.userregister -> {  //用户注册
                val intent = Intent(this, LogupActivity::class.java)
                intent.putExtra("curname",viewModel.curname)
                intent.putExtra("curpass",viewModel.curpass)
                startActivity(intent)
            }
            R.id.usersignout -> {   //退出登录
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("switchboolean",false)
                intent.putExtra("curname","")
                intent.putExtra("curpass","")
                startActivity(intent)
            }
        }
        return true
    }
}