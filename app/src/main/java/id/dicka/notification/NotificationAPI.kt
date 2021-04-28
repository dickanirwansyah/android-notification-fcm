package id.dicka.notification

import id.dicka.notification.Constant.Companion.SERVER_KEY
import id.dicka.notification.Constant.Companion.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification : PushNotificationData): Response<ResponseBody>
}