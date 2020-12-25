package com.example.chat_project.add_group

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chat_project.R
import com.example.chat_project.model.User
import com.example.chat_project.permission
import com.example.chat_project.socket.ChatApplication
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_group.*


@Suppress("DEPRECATION")
class AddGroup : BottomSheetDialogFragment() {
val mSocket= ChatApplication.mSocket!!
 var imgBit:Bitmap?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        a_g_card_img.setOnClickListener {
            permission(
                requireContext(),
                arrayListOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                selectImage()
            }

        }
        a_g_btn.setOnClickListener {
            if (a_g_name.text.toString().isNotEmpty()&&imgBit!=null){
                val id = "group," + System.currentTimeMillis().toString()
                val user = User(
                    id, a_g_name.text.toString(), "", ChatApplication.imageToBase64(
                        imgBit!!
                    ), true
                )
                val uJson = Gson().toJson(user)
                mSocket.emit("user-join", uJson)
              //  val a = Bundle()
              //  a.putParcelable("user", user)
                dismiss()
               // requireActivity().nav_view.isVisible = false
               // MainActivity.navController.navigate(R.id.action_navigation_home_to_chat, a)
            }else{
                Toast.makeText(requireActivity(), "The group name or image is empty", Toast.LENGTH_SHORT).show()

            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 &&
            resultCode == Activity.RESULT_OK
        ) {
            val imageUri = data!!.data
            imgBit= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            a_g_img.setImageURI(imageUri)
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type ="image/*"
        startActivityForResult(intent, 1)
    }

}