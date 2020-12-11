package com.example.chat_project.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_project.R
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.socket.ChatApplication
import kotlinx.android.synthetic.main.recycle_home.view.*

class HomeAdapter(val activity: Activity, private val array: ArrayList<ChatHomeModel>, val click:OnClick) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OnClick{
        fun onClick(chatHomeModel: ChatHomeModel)
        fun onLongClick(id:String)
    }
    override fun getItemCount(): Int {
        return array.size
    }
init {
    Log.e("adapter",array.size.toString())
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.recycle_home, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val chatHomeModel =array[position]


            r_h_name.text=chatHomeModel.name
            r_h_img.setImageBitmap(ChatApplication.decodeImage(chatHomeModel.img))
            r_h_m_last.text= chatHomeModel.lastM
            r_h_time.text=chatHomeModel.time
            holder.itemView.setOnClickListener {
                click.onClick(chatHomeModel)
            }
            holder.itemView.setOnLongClickListener {
                click.onLongClick(chatHomeModel.sId)
                true
            }
            }
        }
    }
