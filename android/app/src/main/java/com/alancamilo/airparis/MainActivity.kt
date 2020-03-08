package com.alancamilo.airparis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import httpclient.request
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            hello_world.text = request()
        }
    }
}
