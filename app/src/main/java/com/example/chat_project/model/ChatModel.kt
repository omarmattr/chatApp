package com.example.chat_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatModel(val sId:String,val mId:String,val message:String,var type:String){
    @PrimaryKey(autoGenerate = true)
    var id :Int= 0
}