package com.dysen.bluetoothautopair

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Compiler.enable
import java.lang.Compiler.enable

class MainActivity : AppCompatActivity(), View.OnClickListener {

    /** Called when the activity is first created.  */
    var autopairbtn: Button? = null
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener(this);
    }

    //设置按钮的监听方法
    override fun onClick(arg0: View) {

        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()//异步的，不会等待结果，直接返回。
        } else {
            bluetoothAdapter.startDiscovery()
        }
    }
}
