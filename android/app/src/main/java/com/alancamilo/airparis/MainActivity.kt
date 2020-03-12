package com.alancamilo.airparis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import http.Client
import http.util.Jour
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            try {
                hello_world.text = Client.requestIndiceJour(Jour.TODAY).toString()
            } catch (error: Throwable) {
                hello_world.text = error.message
            }
        }
    }
}
