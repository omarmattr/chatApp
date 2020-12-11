package com.example.chat_project.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chat_project.db.AppDatabase
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.model.ChatModel
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
    fun getAllChat() = db.gameDao().getAllChat()
    fun insertChat(chat: ChatModel) = viewModelScope.launch {
        Log.e(TAG, "insert")
        db.gameDao().insertChat(chat)
    }

    fun upDateChat(chat: ChatModel) = viewModelScope.launch {
        // Log.e(TAG, "upDate"+chat.toString())
        db.gameDao().upDateChat(chat)
    }

    fun deleteChat(id: String) =
        viewModelScope.launch { Thread { db.gameDao().delete(id) }.start() }

    fun deleteAllChat() = viewModelScope.launch { Thread { db.gameDao().deleteAllChat() }.start() }

    //////////////////////////////////////////////////////////////////
    fun getAllWithId(id: String) = db.gameDao().getAllWithId(id)
    fun getAll() = db.gameDao().getAll()
    fun insert(chat: ChatHomeModel) = viewModelScope.launch {
        Log.e(TAG, "insert")
        db.gameDao().insert(chat)
    }

    fun upDate(chat: ChatHomeModel) = viewModelScope.launch {
        //Log.e(TAG, "upDate"+chat.toString())
        db.gameDao().upDate(chat)
    }

    fun delete(tag: String) = viewModelScope.launch { Thread { db.gameDao().delete(tag) }.start() }
    fun deleteAll() = viewModelScope.launch { Thread { db.gameDao().deleteAll() }.start() }

}