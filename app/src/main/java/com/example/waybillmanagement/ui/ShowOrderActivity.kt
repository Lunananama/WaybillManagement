package com.example.waybillmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waybillmanagement.ui.Adapter.WaybillAdapter
import com.example.waybillmanagement.ui.ViewModel.WaybillViewModel
import kotlinx.android.synthetic.main.activity_show_order.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlin.concurrent.thread

class ShowOrderActivity : BaseActivity() {

    val viewModel by lazy {ViewModelProvider(this).get(WaybillViewModel::class.java)}
    val adapter by lazy { WaybillAdapter(this, viewModel.waybillList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_order)
        setSupportActionBar(showordertoolbar)

        viewModel.curname = intent.getStringExtra("curname").toString()
        viewModel.curpass = intent.getStringExtra("curpass").toString()
        viewModel.cursex = intent.getStringExtra("cursex").toString()
        viewModel.curdepart = intent.getStringExtra("curdepart").toString()

        Log.d("showorder",viewModel.curname)
        Log.d("showorder",viewModel.curpass)
        Log.d("showorder",viewModel.cursex)
        Log.d("showorder",viewModel.curdepart)

        //配置适配器
        val layoutManager = LinearLayoutManager(this)
        showorderrecyclerView.layoutManager = layoutManager
        showorderrecyclerView.adapter = adapter

        //请求数据
        viewModel.querytype = intent.getStringExtra("type").toString()
        if (viewModel.querytype.isNotEmpty()){
            viewModel.getdata(viewModel.querytype)
            supportActionBar?.setTitle("查看" + viewModel.querytype + "运单")
        }else{
            viewModel.waybillList.clear()
            adapter.notifyDataSetChanged()
        }
        Log.d("querytype","querytype: "+ viewModel.querytype)

        //数据变化监听器
        viewModel.waybillListLiveData.observe(this, Observer { result ->
            val waybills = result.getOrNull()
            if (waybills != null){
                showorderrecyclerView.visibility = View.VISIBLE
                viewModel.waybillList.clear()
                viewModel.waybillList.addAll(waybills)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(this,"未能查询到任何货单",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull() ?.printStackTrace()
            }
        })

        //下拉刷新监听器
        showorderswiperefresh.setColorSchemeResources(R.color.purple_200)  //设置下拉刷新进度条的颜色
        showorderswiperefresh.setOnRefreshListener {
            refreshWaybills(adapter)
        }

        //设置滑动菜单栏导航按钮
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)          //让导航按钮显示出来
            it.setHomeAsUpIndicator(R.drawable.ic_menu) //设置导航栏图标
        }

        //滑动菜单项监听器
        showordernavView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.navAddOrder -> {
                    Toast.makeText(this,"addorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddOrderActivity::class.java)
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("MainActivity","addorder is clicked")
                }

                R.id.navJSONOrder -> {
                    Toast.makeText(this,"jsonorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","json")
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("MainActivity","JSONorder is clicked")
                }

                R.id.navXMLOrder -> {
                    Toast.makeText(this,"xmlorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","xml")
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("MainActivity","XMLorder is clicked")
                }

                R.id.navLOCOrder -> {
                    Toast.makeText(this,"locorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","loc")
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

            showorderdrawyerlayout.closeDrawers()
            true
        }

        //返回
        showorderbackButton.setOnClickListener {
            this.finish()
            Log.d("showorderActivity","显示运单页面关闭")
        }

    }

    override fun onRestart() {
        super.onRestart()
        refreshWaybills(adapter)
    }

    //下拉刷新处理逻辑：去网络上请求最新的数据
    private fun refreshWaybills(adapter: WaybillAdapter){
        thread {
            Thread.sleep(2000)
            runOnUiThread{
                viewModel.querytype = intent.getStringExtra("type").toString()
                if (viewModel.querytype.isNotEmpty()){
                    viewModel.getdata(viewModel.querytype)
                    supportActionBar?.setTitle("查看" + viewModel.querytype + "运单")
                }else{
                    viewModel.waybillList.clear()
                    adapter.notifyDataSetChanged()
                }
                showorderswiperefresh.isRefreshing = false
            }
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
            android.R.id.home -> showorderdrawyerlayout.openDrawer(GravityCompat.START) //打开滑动菜单
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