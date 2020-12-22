package com.example.chat_project.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_project.add_group.AddGroup
import com.example.chat_project.MainActivity
import com.example.chat_project.R
import com.example.chat_project.adapter.HomeAdapter
import com.example.chat_project.model.ChatHomeModel
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(),HomeAdapter.OnClick {
   private val mSocket=ChatApplication.mSocket
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        requireActivity().nav_view.isVisible = true
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_float.setOnClickListener {
            showBottomSheet()
            //  MainActivity.navController.navigate(R.id.action_navigation_home_to_navigation_online)

        }
        viewModel.getAll().observe(viewLifecycleOwner, {array ->

            h_recycle.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = HomeAdapter(requireActivity(), array as ArrayList, this@HomeFragment)
            }

        })

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()

    }
    private fun showBottomSheet(){
      val   addPhotoBottomDialogFragment =
            AddGroup()
        if (!addPhotoBottomDialogFragment.isAdded){
            addPhotoBottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                ""
            )}else{addPhotoBottomDialogFragment.dismiss()

            addPhotoBottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }
    }
    override fun onClick(h: ChatHomeModel) {
        requireActivity().nav_view.isVisible = false
        val a = Bundle()
        a.putInt("chat_id",h.id)
        a.putParcelable("user", User(h.sId,h.name!!,h.name,"",true))
        Log.e("oooH",h.sId+MainActivity.mId)
        MainActivity.navController.navigate(R.id.action_navigation_home_to_chat, a)
    }

    override fun onLongClick(id: String) {
        viewModel.delete(id)
    }
}