package com.example.chat_project.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chat_project.R
import com.example.chat_project.ui.login.Login
import com.example.chat_project.ui.signup.Signup
import kotlinx.android.synthetic.main.fragment_splash.*

class Splash : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        s_signup.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main2_container, Signup()).addToBackStack(null).commit()
        }
        s_login.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main2_container, Login()).addToBackStack(null).commit()
        }
    }
}