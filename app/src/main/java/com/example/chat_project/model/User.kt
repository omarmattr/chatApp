package com.example.chat_project.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (val id:String,val name:String,val email:String,val img:String,var unline:Boolean):Parcelable