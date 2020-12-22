package com.example.chat_project.ui.chat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_project.MainActivity
import com.example.chat_project.R
import com.example.chat_project.adapter.ChatAdapter
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.model.ChatModel
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import com.example.chat_project.socket.ChatApplication.Companion.imageToBase64
import com.example.chat_project.ui.home.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Chat : Fragment(), TextWatcher {
    var chatId:Int?=null
    val TAG = ChatApplication.TAG + "_Chat"
    var mSocket = ChatApplication.mSocket
    lateinit var user: User
    private val myAdapter by lazy {
        ChatAdapter(requireActivity())
    }
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argum = arguments
        chatId =  argum?.getInt("chat_id")
        if (argum?.getParcelable<User>("user") != null)
            user = argum.getParcelable("user")!!
        arguments?.clear()
        chat_name.text = user.name
        val layout=LinearLayoutManager(requireActivity())
        recycle_chat.apply {
            layout.reverseLayout=true
            layout.stackFromEnd=true

            layoutManager = layout
            adapter = myAdapter
        }
        viewModel.getAllChatWithId(user.id).observe(viewLifecycleOwner, { chat_model ->
            if (!chat_model.isNullOrEmpty()) {
              //  Log.e(TAG, chat_model.toString())
                myAdapter.array.clear()
                if (!chat_model.isNullOrEmpty()) {
                    chat_model.forEach { myAdapter.add(it) }
                    layout.scrollToPosition(0)
                    myAdapter.notifyDataSetChanged()
                }
            }
//
        })

        img_send.setOnClickListener {
            val message = ed_messege.text.toString().trim { it <= ' ' }
            if (message.isNotEmpty()) {
                send(user.id,MainActivity.mId, message, "s,text")
            }
        }

        ed_messege.addTextChangedListener(this)
        ch_attachment.setOnClickListener {
            ChatApplication.getImg(this)
        }
        var mid = MainActivity.mId
        if (user.id.split(",")[0] == "group") {
            mid = user.id
        }
        receive(mid)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                // o_img.setImageURI(fileUri)

                //You can get File object from intent
                val file: File = ImagePicker.getFile(data)!!
                val imgBit =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                GlobalScope.launch(Dispatchers.Main) {
                    send(
                        user.id,
                        MainActivity.mId,
                        imageToBase64(imgBit),
                        "s,img"
                    )
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun receive(id: String) {
        mSocket!!.on(id, fun(data) {
            GlobalScope.launch(Dispatchers.Main) {
                val message = data[0] as JSONObject
                val chat = ChatModel(
                    message.getString("sId"),
                    message.getString("mId"),
                    message.getString("data"),
                    message.getString("type")
                )
                val newType=chat.type.split(",")[1]

               // Log.e("TAG", chat.type)
                if (user.id ==chat.mId) {
                    val chatP = ChatModel(
                        message.getString("mId"),
                        message.getString("sId"),
                        message.getString("data"),
                        message.getString("type")
                    )
                    val newTypeP=chatP.type.split(",")[1]
                    chatP.type= "r,$newTypeP"
                    viewModel.insertChat(chatP)
                    myAdapter.notifyDataSetChanged()
                    Log.e("TAG", "ooi $chat")
                } else if (user.id.split(",")[0] == "group") {
                    if (MainActivity.mId != message.getString("mId")) {
                        chat.type= "r,$newType"}
                        Log.e("omarmattr", chat.toString())
                        viewModel.insertChat(chat)
                        myAdapter.notifyDataSetChanged()

                }
            }
        })
    }

    fun send(sId: String, mId: String, message: String, type: String) {
        Log.e("omarmattr", "send")
        ed_messege.setText("")
        if (myAdapter.array.isEmpty())viewModel.getImage(sId).observe(viewLifecycleOwner,{
            viewModel.insert(ChatHomeModel(sId, user.name,it, "", ""))
        })

        val chatModel=ChatModel(sId, mId, message, type)
      if (sId.split(",")[0] != "group")  viewModel.insertChat(chatModel)
        Log.e("TAG", chatModel.toString())
        mSocket!!.emit("message", sId, mId, message, type)
        myAdapter.notifyDataSetChanged()

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        ch_attachment.isVisible = ed_messege.text.isEmpty()
    }

    override fun afterTextChanged(p0: Editable?) {
    }
    @SuppressLint("SimpleDateFormat")
    fun getNowDate(): String {
        return if (Build.VERSION.SDK_INT < 26) {
            val currentTime = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")//,Locale.getDefault())
            //  simpleDateFormat.timeZone = TimeZone.getDefault()
            simpleDateFormat.format(currentTime)
        } else {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()

    }

    override fun onStart() {
        if (myAdapter.array.isNotEmpty() && chatId!=null) {
            viewModel.getImage(user.id).observe(viewLifecycleOwner,{img->
                Log.e("ooo", "chatId is Not Null Or Empty")
                val message =
                    if (myAdapter.array.last().type.split(",")[1] == "text") myAdapter.array.last().message else "image"
                val chatHomeModel=ChatHomeModel(user.id, user.name,img, message, getNowDate())
                chatHomeModel.id=chatId!!
                viewModel.upDate(chatHomeModel)
            })
        }
        super.onStart()
    }
    override fun onDestroy() {


        super.onDestroy()
}

}