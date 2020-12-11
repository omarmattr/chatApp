package com.example.chat_project.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chat_project.R
import com.example.chat_project.socket.ChatApplication
import com.example.chat_project.ui.login.Login
import kotlinx.android.synthetic.main.fragment_signup.*


class Signup : Fragment(),TextWatcher {
    var mSocket= ChatApplication.mSocket
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signup_username.addTextChangedListener(this)
        signup_email.addTextChangedListener(this)
        signup_password.addTextChangedListener(this)
        signup_co_password.addTextChangedListener(this)

        signup_btn_up.setOnClickListener {
            val name =signup_username.text.toString()
            val email=signup_email.text.toString()
            val password=signup_co_password.text.toString()
            val img=null

            mSocket.emit("user-insert",name,email,password,img)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main2_container, Login()).addToBackStack(null).commit()
        }
        signup_btn_in.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main2_container, Login()).addToBackStack(null).commit()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        var a= true
        if (signup_username.text.isEmpty()||  signup_username.text.toString().length<6){
            signup_username.error="field is empty or short"
            a =  false
        }else
         if (signup_email.text.isEmpty()){
             signup_username.error=null
             signup_email.error="field is empty or short"
             a =false
        }else
       if (signup_password.text.isEmpty()|| signup_password.text.toString().length<6){
           signup_email.error=null
           signup_password.error="field is empty or short"
           a = false
        }else
      if (signup_password.text.toString()!=signup_co_password.text.toString()){
          signup_password.error=null
          signup_co_password.error="password dose not match"
          a=false
      }
        signup_co_password.error= null
        signup_btn_up.isEnabled=a
    }

    override fun afterTextChanged(s: Editable?) {
    }
}