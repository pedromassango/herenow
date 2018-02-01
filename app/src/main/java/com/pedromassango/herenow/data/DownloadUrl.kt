package com.pedromassango.herenow.data

import com.pedromassango.herenow.app.HereNow.Companion.logcat
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by Pedro Massango on 1/21/18.
 *
 * Class to download places from Google Places API.
 */
object DownloadUrl {

    @Throws(Exception::class)
    fun readUrl(mUrl: String): String {
        var data = ""
        var inputStrean: InputStream? = null
        var httpUrlConnection: HttpURLConnection? = null

        try {
            val url = URL(mUrl)

            // create a connction to comunicate with the url
            httpUrlConnection = url.openConnection() as HttpURLConnection

            // connect with the url
            httpUrlConnection.connect()

            // read data from url
            inputStrean = httpUrlConnection.inputStream

            val bufferReader = BufferedReader(InputStreamReader(inputStrean))

            val sb = StringBuffer()

            var line: String? = null
            while ({ line = bufferReader.readLine(); line }() != null) {
                //line = bufferReader.readLine()
                sb.append(line)
            }

            data = sb.toString()
            logcat("DownloadUrl: -> $data")
            bufferReader.close()


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStrean!!.close()
            httpUrlConnection!!.disconnect()
        }
        return data
    }
}