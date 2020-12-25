package com.example.chat_project.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_project.R
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import kotlinx.android.synthetic.main.recycle_home.view.*

class UnLineUserAdapter(private val activity:Activity, var array: ArrayList<User>, val click:OnClick) : RecyclerView.Adapter<UnLineUserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface OnClick{
    fun onClick(user: User)
}
    override fun getItemCount()=array.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=ViewHolder(LayoutInflater.from(activity).inflate(R.layout.recycle_home, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("oooab",array.size.toString())
       holder.itemView.apply {
           r_h_m_last.isVisible=false
           r_h_img.setImageBitmap(ChatApplication.decodeImage(array[position].img))
           r_h_name.text=array[position].name
           r_h_name.setOnClickListener {
               val user =array[position]
               click.onClick(user)
           }
       }
    }

}