package com.example.chat_project

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = ChatApplication.TAG+"_Main"
    private lateinit var user:User
    companion object{
        lateinit var navController:NavController
        var mId = ""
    }
    private val mSocket=ChatApplication.mSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         navController = findNavController(R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)

    }
    override fun onResume() {
        super.onResume()
        val  sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
        mId=sharedPref.getString("user_id","")!!
        user = User(
            mId,
            sharedPref.getString("username","")!!,
            sharedPref.getString("email","")!!,
            sharedPref.getString("img","")!!,true)
        joinServer()
    }



    private fun joinServer() {
        user.unline=true
        val json =  Gson().toJson(user)
        mSocket.emit("user-online", json)
    }
}