package com.example.chat_project.ui.online

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_project.MainActivity
import com.example.chat_project.R
import com.example.chat_project.adapter.UnLineUserAdapter
import com.example.chat_project.model.ImageModel
import com.example.chat_project.model.User
import com.example.chat_project.socket.ChatApplication
import com.example.chat_project.ui.home.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_online.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.reflect.Type

class OnlineFragment : Fragment(), UnLineUserAdapter.OnClick {
    var mSocket= ChatApplication.mSocket
    lateinit var myAdapter: UnLineUserAdapter
    private lateinit var dashboardViewModel: OnlineViewModel
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    val arrayUser = ArrayList<User>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(OnlineViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_online, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().nav_view.isVisible = true

       mSocket.emit("user-get", MainActivity.mId)
        userOnline().also {
            myAdapter = UnLineUserAdapter(requireActivity(), arrayUser, this)
            Log.e("oooD", arrayUser.size.toString())
            recycle_unline.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = myAdapter
            }

        }

    }

    private fun userOnline() {

      mSocket.on("new-users", fun(data) {
            GlobalScope.launch(Dispatchers.Main) {
                val jUsers = data[0] as JSONObject
                val a = jUsers.getJSONArray("array")
                arrayUser.clear()
                val userListToken: Type = object : TypeToken<User>() {}.type
                for (i in 0 until a.length()) {
                    val userList =
                        Gson().fromJson<User>(a[i].toString(), userListToken)
                    arrayUser.add(userList)
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    arrayUser.removeIf {
                        it.id == MainActivity.mId
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })

    }

    override fun onClick(user: User) {
        requireActivity().nav_view.isVisible = false
        viewModel.getAllImageId().observe(viewLifecycleOwner, Observer{
            val isId=it.find{id-> id == user.id}
            if (isId.isNullOrEmpty())viewModel.insertImage(ImageModel(user.id,user.img))
            user.img=""
            val a = Bundle()
            a.putParcelable("user", user)
            MainActivity.navController.navigate(R.id.action_navigation_online_to_chat, a)
        })

    }

}