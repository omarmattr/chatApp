package com.example.chat_project.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chat_project.db.AppDatabase
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.model.ChatModel
import com.example.chat_project.model.ImageModel
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = ChatApplication.TAG + "_HM"

    init {
        Log.e(TAG, "init")
    }

    val userLiveData = MutableLiveData<User>()
    val db by lazy { AppDatabase.invoke(application.applicationContext) }

    fun getAllChatWithId(id: String) = db.gameDao().getAllChatWithId(id)
    fun insertChat(chat: ChatModel) = viewModelScope.launch { db.gameDao().insertChat(chat) }
    fun upDateChat(chat: ChatModel) = viewModelScope.launch { db.gameDao().upDateChat(chat) }
    fun deleteChat(id: String) = viewModelScope.launch { db.gameDao().delete(id) }
    fun deleteAllChat() = viewModelScope.launch {  db.gameDao().deleteAllChat() }
    //////////////////////////////////////////////////////////////////
    fun getAllWithId(id: String) = db.gameDao().getAllWithId(id)
    fun getAll() = db.gameDao().getAll()
    fun insert(chat: ChatHomeModel) = viewModelScope.launch { db.gameDao().insert(chat) }
    fun upDate(chat: ChatHomeModel) = viewModelScope.launch { db.gameDao().upDate(chat) }
    fun delete(tag: String) = viewModelScope.launch {  db.gameDao().delete(tag) }
    fun deleteAll() = viewModelScope.launch { db.gameDao().deleteAll() }
///////////////////////////////////////////////////////////////////////
    fun getImage(id: String) = db.gameDao().getImage(id)
    fun getAllImageId() = db.gameDao().getAllImageId()
    fun insertImage(imageModel: ImageModel) = viewModelScope.launch { db.gameDao().insertImage(imageModel) }
    fun upDate(imageModel: ImageModel) = viewModelScope.launch { db.gameDao().upDateImage(imageModel) }
    fun deleteImage(tag: String) = viewModelScope.launch {  db.gameDao().deleteImage(tag) }
}