package com.example.chat_project.ui.other

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chat_project.R
import com.example.chat_project.Registration
import com.example.chat_project.model.ImageModel
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import com.example.chat_project.socket.ChatApplication.Companion.decodeImage
import com.example.chat_project.socket.ChatApplication.Companion.imageToBase64
import com.example.chat_project.ui.home.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_other.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray


@Suppress("DEPRECATION")
class Other : Fragment() {


    var mSocket = ChatApplication.mSocket!!
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_other, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        o_img.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) { ChatApplication.getImg(this@Other) }

        }
        lateinit var user: User
        val sharedPref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val name = sharedPref.getString("username", "")
        viewModel.userLiveData.observe(viewLifecycleOwner,{
            o_txt_name.text = it.name
            viewModel.getImage(it.id).observe(viewLifecycleOwner,{image->
                if (!image.isNullOrEmpty())
                    GlobalScope.launch(Dispatchers.Main) {o_img.setImageBitmap(decodeImage(image))}
                else  sharedPref.edit().putBoolean("isImage",false).apply()

            })

        })

        mSocket.emit(
            "user-get-data",
            name
        )
        mSocket.on(name, fun(value) {
            val jUsers = value[0] as JSONArray
            val userJO = jUsers.getJSONObject(0)

            user = User(
                userJO.getString("id"),
                userJO.getString("name"),
                userJO.getString("email"),
                userJO.getString("img"),
                true
            )

            if (!sharedPref.getBoolean("isImage",true))viewModel.insertImage(ImageModel(user.id, userJO.getString("img")))

            viewModel.userLiveData.postValue(user)
        })

        o_btn_delete.setOnClickListener {
            viewModel.deleteAll()
        }
        o_btn_log_out.setOnClickListener {
            viewModel.deleteAll()
            viewModel.deleteAllChat()
            sharedPref.edit().clear().apply()
              user.unline=false
            val json =  Gson().toJson(user)
             mSocket.emit("user-online", json)
           //  mSocket.disconnect()
           //   mSocket.close()
            requireActivity().startActivity(Intent(requireActivity(), Registration::class.java))
            requireActivity().finish()
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                o_img.setImageURI(fileUri)

               val imgBit =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                val sharedPref =
                    requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                val name = sharedPref.getString("username", "")
                sharedPref.edit().putBoolean("isImage",true).apply()
                mSocket.emit("load-user-img", name, imageToBase64(imgBit))
                Log.e("ooooo", imageToBase64(imgBit))
                //  Log.e("other", imageToBase64(imgBit))
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


}