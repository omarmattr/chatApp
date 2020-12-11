package com.example.chat_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class ChatHomeModel(val sId:String,val name:String?, val img:String ,val lastM:String, val time:String){
    @PrimaryKey(autoGenerate = true)
    var id :Int= 0
}