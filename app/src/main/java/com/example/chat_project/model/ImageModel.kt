package com.example.chat_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageModel(val userId:String,val img:String){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
