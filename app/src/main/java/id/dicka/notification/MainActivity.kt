package id.dicka.notification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnSendNotification = findViewById(R.id.btnSendNotification) as Button
        var edtEnterTitle = findViewById(R.id.edtEnterTitle) as EditText
        var edtEnterMessage = findViewById(R.id.edtEnterMessage) as EditText
        var edtEnterToken = findViewById(R.id.edtEnterToken) as EditText

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            System.out.println("TOKEN : "+it.token)
            FirebaseService.token = it.token
            edtEnterToken.setText(it.token)
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btnSendNotification.setOnClickListener {
            val title = edtEnterTitle.text.toString()
            val message = edtEnterMessage.text.toString()
            val token = edtEnterToken.text.toString()

            if (title.isNotEmpty() && message.isNotEmpty() && token.isNotEmpty()){
                PushNotificationData(
                        NotificationData(title, message),
                        token
                ).also {
                    sendNotification(it)
                }
            }
        }

    }

    private fun sendNotification(notification: PushNotificationData) = CoroutineScope(Dispatchers.IO).launch {
         try{
            val response = RetrofitInstance.api.postNotification(notification)
             if (response.isSuccessful){
                 Log.d(TAG, "Response: ${Gson().toJson(response)}")
             }else{
                 Log.e(TAG, response.errorBody().toString())
             }
         }catch (e: Exception){
            Log.e(TAG, e.toString())
         }
    }

}