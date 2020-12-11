package com.example.chat_project.db

import androidx.room.TypeConverter
import com.example.chat_project.model.ChatModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    //    @TypeConverter
//    fun mapToString(map: MutableMap<Int,Int>): String {
//        return Gson().toJson(map)
//    }
//
//    @TypeConverter
//    fun stringToMap(string: String?): MutableMap<Int,Int> {
//        return Gson().fromJson(string,object : TypeToken< Map<Int, Int>>(){}.type)
//    }
    @TypeConverter
    fun chatMToString(p: ChatModel): String {
        return Gson().toJson(p)
    }

    @TypeConverter
    fun stringToChatM(string: String?): ChatModel {
        return Gson().fromJson(string,ChatModel::class.java)
    }
//    @TypeConverter
//    fun chatToString(p: ArrayList<ChatModel>): String {
//        return Gson().toJson(p)
//    }
//
//    @TypeConverter
//    fun stringToChat(string: String?): ArrayList<ChatModel> {
//        return Gson().fromJson(string, object : TypeToken<ArrayList<ChatModel>>() {}.type)
//    }
}





