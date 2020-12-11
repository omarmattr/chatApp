package com.example.chat_project.ui.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.chat_project.R
import com.example.chat_project.socket.ChatApplication
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfile  : BottomSheetDialogFragment() {
        val mSocket= ChatApplication.mSocket

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_edit_profile, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            edit_p_change.setOnClickListener {
               edit_p_password.isVisible=true
                edit_p_new_password.isVisible=true
            }
            edit_p_btn.setOnClickListener {
                val name =edit_p_name.text.toString()
                if (edit_p_password.isVisible){

                }else{

                }
            }

        }
}