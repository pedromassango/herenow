package com.pedromassango.herenow.extras

import android.os.AsyncTask
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Pedro Massango on 04/06/2017.
 */

object NotificationSender {

    enum class NotificationType{  GERAL, ADDED_AS_FRIEND}

    // My Firebase sender id
    private const val SENDER_ID = "AAAAJcrdEr8:APA91bGy-Y4Yag7Kvl42DHFhr-XnJcxi4bWXrfQuK-88hGo_NbcWJw1Sv8NEbWPSzuA5FDnBhi-oRoKB9BjwG4acMNPaffpYxWtwNF0vbyoe7dOhAt7dx9ArEFyQgwvpW_trDCIYlNr4"
    private const val API_URL_FCM = "https://fcm.googleapis.com/fcm/send"
    private const val TAG = "NotificationSender"

    private val firebaseMessaging = FirebaseMessaging.getInstance()

    fun send(notificationType: NotificationType,
             friendNumber: String,
             senderNumber: String) {
        Log.v(TAG, "sending Notification")

        val data = HashMap<String, Any>()
        data[Constants.SENDER_NUMBER] = senderNumber
        data[Constants.NOTIFICATION_TYPE] = notificationType

        // send the notification
        SendFcm(data, friendNumber).execute()
    }

    fun subscribe(mTopic: String) {
        Log.v(TAG, "subscribe: " + mTopic)
        val topic = Utils.getFormatedNumber(mTopic)
        firebaseMessaging.subscribeToTopic(topic)
    }

    class SendFcm(private val data: HashMap<String, Any>,
                  private val topic: String) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {
            try {

                // send fcm message
                val url = URL(API_URL_FCM)
                val connection = url.openConnection() as HttpURLConnection

                connection.useCaches = false
                connection.doInput = true
                connection.doOutput = true
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "key=" + SENDER_ID)
                connection.setRequestProperty("Content-Type", "application/json")

                val json = JSONObject()
                json.put("to", "/topics/" + Utils.getFormatedNumber(topic))
                val info = JSONObject()
                info.put(Constants.NOTIFICATION_DATA, data)
                // json.put("notification", data)

                val writer = OutputStreamWriter(connection.outputStream)

                writer.write(json.toString())
                writer.flush()
                connection.inputStream
                //writer.close();
                Log.v(TAG, "sendNotification -> success")
                Log.v(TAG, "sendNotification -> success")
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.v(TAG, "sendNotification -> error")
                Log.v(TAG, "sendNotification -> error")
            }
        }
    }

}
