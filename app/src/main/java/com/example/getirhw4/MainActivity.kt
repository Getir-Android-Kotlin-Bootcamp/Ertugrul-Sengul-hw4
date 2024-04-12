package com.example.getirhw4

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.Buffer

class MainActivity : AppCompatActivity() {

    private val baseUrl ="https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/"
    private lateinit var buttonGetProfile: Button
    private lateinit var buttonLogin: Button
    private lateinit var editTextUserID: EditText
    private lateinit var textViewUserProfile: TextView
    private lateinit var textViewUserLogin: TextView
    private lateinit var editTextUserEmail: EditText
    private lateinit var editTextUserPassword: EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buttonGetProfile = findViewById<Button>(R.id.button_get_profile)
        buttonLogin = findViewById<Button>(R.id.button_login)
        editTextUserID = findViewById(R.id.editText_userID)
        textViewUserLogin = findViewById(R.id.textview_loginInfo)
        textViewUserProfile = findViewById(R.id.textview_profile)
        editTextUserEmail = findViewById(R.id.editText_email)
        editTextUserPassword = findViewById(R.id.editText_password)

        buttonGetProfile.setOnClickListener {
            val userID = editTextUserID.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response = getUserProfile(userID)

                withContext(Dispatchers.Main){
                    textViewUserProfile.text = response
                }
            }
        }

        buttonLogin.setOnClickListener {
            val userLoginDto =
                UserLoginDto(
                    editTextUserEmail.text.toString(),
                    editTextUserPassword.text.toString()
                )

            CoroutineScope(Dispatchers.IO).launch {
                val response = login(userLoginDto)

                withContext(Dispatchers.Main){
                    textViewUserLogin.text = response
                }
            }
        }
    }

    // Send GET request to the backend to get user profile
    private fun getUserProfile(
        userID: String,
    ): String {
        // For test case, created user id: ae41278c-8128-40c3-b6d8-7d23c6b21218

        val urlPath = baseUrl + "profile" + "/" + userID
        val mURL = URL(urlPath)
        val connection = mURL.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        with(connection){
            if (responseCode == HttpURLConnection.HTTP_OK){
                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
                var temp = ""
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        println(line)
                        temp = line
                    }
                }
                return temp
            }else{
                //404 Not Found
                return "404 Not Found"
            }
        }
    }

    // Send POST request a user login dto and returns user id
    private fun login(userLoginDto: UserLoginDto): String {
        var userID = ""
        val urlPath = baseUrl + "login"
        val mURL = URL(urlPath)

        val jsonObject = JSONObject().apply {
            put("email", userLoginDto.email)
            put("password", userLoginDto.password)
        }

        val connection = mURL.openConnection() as HttpURLConnection
        connection.apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            doInput = true
            doOutput = true
        }

        val writer = OutputStreamWriter(connection.outputStream)
        try {
            writer.write(jsonObject.toString())
            writer.flush()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use {
                    it.readText()
                }
                println("Response: $response")
                userID = response
            } else {
                val errorMessage = connection.errorStream.bufferedReader().use {
                    it.readText()
                }
                println("Error: $errorMessage")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            writer.close()
            connection.disconnect()
        }

        return userID
    }

}