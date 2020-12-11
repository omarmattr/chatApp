package com.example.chat_project.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_project.R
import com.example.chat_project.model.ChatModel
import com.example.chat_project.socket.ChatApplication
import kotlinx.android.synthetic.main.recycle_chat_recive.view.*
import kotlinx.android.synthetic.main.recycle_chat_send.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatAdapter(var activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class Recive(itemView: View) : RecyclerView.ViewHolder(itemView)
    class Sender(itemView: View) : RecyclerView.ViewHolder(itemView)

    val send = 0
    val receive = 1
    var array = ArrayList<ChatModel>()
    fun add(chatModel: ChatModel) {
        array.add(0,chatModel)


    }

    override fun getItemViewType(position: Int): Int {
        return if (array[position].type.split(",")[0] == "s")
            send else receive
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            receive -> Recive(
                LayoutInflater.from(activity).inflate(R.layout.recycle_chat_recive, parent, false)
            )
            send -> Sender(
                LayoutInflater.from(activity).inflate(R.layout.recycle_chat_send, parent, false)
            )
            else -> Recive(
                LayoutInflater.from(activity).inflate(R.layout.recycle_chat_send, parent, false)
            )
        }

    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == send -> {

                (holder as Sender).itemView.apply {
                    Log.e("type", array[position].type)

                    when (array[position].type.split(",")[1]) {
                        "text" -> r_s_text.text = array[position].message

                        "img" -> {
                            r_s_text.isVisible = false
                            r_s_img_card.isVisible = true
                            GlobalScope.launch(Dispatchers.Main) {
                                r_s_img.setImageBitmap(ChatApplication.decodeImage(array[position].message))
                            }
                        }
                    }
                }
            }
            getItemViewType(position) == receive -> {
                (holder as Recive).itemView.apply {
                    when (array[position].type.split(",")[1]) {
                        "text" -> r_r_text.text = array[position].message
                        "img" -> {
                            r_r_text.isVisible = false
                            r_r_img_card.isVisible = true
                            r_r_img.setImageBitmap(ChatApplication.decodeImage(array[position].message))
                        }
                    }


                }

            }
        }


    }
}