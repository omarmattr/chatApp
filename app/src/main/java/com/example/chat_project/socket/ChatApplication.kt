package com.example.chat_project.socket;

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.chat_project.db.AppDatabase
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.io.ByteArrayOutputStream

import java.net.URISyntaxException;

 class ChatApplication:Application() {

    fun  getSocket():Socket {
        return mSocket
    }

     companion object{
         fun getImg(fragment: Fragment) {
             ImagePicker.with(fragment)
                 .crop()                    //Crop image(Optional), Check Customization for more option
                 .compress(1024)            //Final image size will be less than 1 MB(Optional)
                 .maxResultSize(
                     720,
                     720
                 )    //Final image resolution will be less than 1080 x 1080(Optional)
                 .start()
         }
         fun imageToBase64(bitmap: Bitmap): String {
             val byteArrayOutputStream = ByteArrayOutputStream()
             bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
             return Base64.encodeToString(
                 byteArrayOutputStream.toByteArray(),
                 Base64.DEFAULT
             )
         }

         fun decodeImage(data: String): Bitmap? {
             val decodedString: ByteArray = Base64.decode(data, Base64.DEFAULT)
             return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
         }
         val TAG="omar"
         val mSocket=  IO.socket("http://10.7.12.138:2000")
     }

     override fun onCreate() {
         super.onCreate()
         AppDatabase.invoke(this)
         mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
         mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
         mSocket.on(Socket.EVENT_CONNECT, onConnect)
         mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect)
         mSocket.connect()
     }
     var onConnect = Emitter.Listener {
         Log.e(TAG, "Socket Connected!")
     }
         private val onConnectError: Emitter.Listener = Emitter.Listener {
     }

     private val onDisconnect: Emitter.Listener = Emitter.Listener {
     }

 }
