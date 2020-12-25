package com.example.chat_project.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.model.ChatModel
import com.example.chat_project.model.ImageModel

@Dao
interface GameDao {
//Chat
    @Query("SELECT * FROM chatmodel where sId=:id ")
    fun getAllChatWithId(id: String): LiveData<List<ChatModel>>
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
    //ChatHome
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
    /////////////////////////////////////////////////////////////////////////
    //Image
    @Query("SELECT img FROM imagemodel where userId=:id ")
    fun getImage(id: String): LiveData<String>
    @Query("SELECT userId FROM imagemodel  ")
    fun getAllImageId(): LiveData<List<String>>
    @Insert
    suspend fun insertImage(vararg imageModel: ImageModel)
    @Update
    suspend fun upDateImage(vararg imageModel: ImageModel)
    @Query("delete from imagemodel where userId=:id")
    fun deleteImage(id: String)

}