package com.example.chat_project.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.model.ChatModel

@Dao
interface GameDao {

    @Query("SELECT * FROM chatmodel where sId=:id ")
    fun getAllChatWithId(id: String): LiveData<List<ChatModel>>

    @Query("SELECT * FROM chatmodel ")
    fun getAllChat(): LiveData<List<ChatModel>>

    @Insert
    suspend fun insertChat(vararg chat: ChatModel)

    @Update
    suspend fun upDateChat(vararg chat: ChatModel)

    @Query("delete from chatmodel where sId=:id")
    fun deleteChat(id: String)

    @Query("delete from chatmodel where id=:id")
    fun deleteOneChat(id: String)

    @Query("delete from chatmodel")
    fun deleteAllChat()
//////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM chathomemodel where sId=:id ")
    fun getAllWithId(id: String): LiveData<List<ChatHomeModel>>
    @Query("SELECT * FROM chathomemodel ")
    fun getAll(): LiveData<List<ChatHomeModel>>

    @Insert
    suspend fun insert(vararg chat: ChatHomeModel)

    @Update
    suspend fun upDate(vararg chat: ChatHomeModel)

    @Query("delete from chathomemodel where sId=:id")
    fun delete(id: String)
    @Query("delete from chathomemodel")
    fun deleteAll()

}