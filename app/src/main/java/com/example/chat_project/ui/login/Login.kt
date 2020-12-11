package com.example.chat_project.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chat_project.MainActivity
import com.example.chat_project.R
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import com.example.chat_project.ui.signup.Signup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray


class Login : Fragment(), TextWatcher {

    var mSocket = ChatApplication.mSocket
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signin_username.addTextChangedListener(this)

        signin_password.addTextChangedListener(this)
        signin_btn_in.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
            mSocket!!.emit(
                "user-login",
                signin_username.text.toString(),
                signin_password.text.toString()
            )
            mSocket!!.on(signin_username.text.toString(), fun(value) {
                GlobalScope.launch(Dispatchers.Main) {
                    val jUsers = value[0] as JSONArray
                    Log.e("login", jUsers.length().toString())
                    if (jUsers.length() == 0) {
                        Toast.makeText(
                            requireContext(),
                            "username or password dose not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val userJO = jUsers.getJSONObject(0)
                        val user = User(
                            userJO.getString("id"),
                            userJO.getString("name"),
                            userJO.getString("email"),
                            userJO.getString("img"),
                            true
                        )
                        val json = Gson().toJson(user)
                        mSocket!!.emit(
                            "user-join", json
                        )
                        val editor = sharedPref.edit()
                        editor.putBoolean("login", true)
                        editor.putString("user_id", user.id)
                        editor.putString("username", user.name)
                        editor.putString("email", user.email)
                        editor.putString("img", user.img)
                        editor.apply()
                        requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()

                    }
                }
            })


        }
        signin_btn_up.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main2_container, Signup()).addToBackStack(null).commit()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        var a = true
        if (signin_username.text.isEmpty() || signin_username.text.toString().length < 6) {
            signin_username.error = "field is empty or short"
            a = false
        } else
            if (signin_password.text.isEmpty() || signin_password.text.toString().length < 6) {
                signin_username.error = null
                signin_password.error = "field is empty or short"
                a = false
            }
        signin_password.error = null
        signin_btn_in.isEnabled = a
    }

    override fun afterTextChanged(s: Editable?) {
    }


}