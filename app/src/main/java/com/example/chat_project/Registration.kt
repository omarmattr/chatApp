package com.example.chat_project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chat_project.socket.ChatApplication
import com.example.chat_project.ui.splash.Splash
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main2.*

class Registration : AppCompatActivity() {

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
        if (sharedPref.getBoolean("login",false)){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.main2_container,Splash()).commit()
        }



    }

}