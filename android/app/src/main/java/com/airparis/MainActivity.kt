package com.airparis

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import airparis.data.http.AirparifAPI
import airparis.data.http.model.util.Day
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                hello_world.text = AirparifAPI()
                    .requestDayIndex(Day.TODAY).toString()
            } catch (error: Throwable) {
                Log.e("MAIN_ACTIVITY", error.message)
            }
        }
    }
}
