package com.test.testapplicationpingocean

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isClientError
import com.github.kittinunf.fuel.core.isServerError
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.gson.jsonBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.testapplicationpingocean.databinding.ActivityMainBinding
import com.test.testapplicationpingocean.request.RequestModel
import com.test.testapplicationpingocean.request.ResponseList
import com.test.testapplicationpingocean.settingsStorage.SettingCheckerStorageImpl

var tokenForProfile : String? = null
lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val storage = SettingCheckerStorageImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage.setChecked(newValue = false)
        storage.saveChanges()
        storage.init(applicationContext)
        Log.e("TTT", storage.getChecked().toString())

        binding.button.setOnClickListener {
            sendRequest()
        }

    }

    override fun onStart() {
        super.onStart()
        if (storage.getChecked()) {
            startActivity(Intent(this@MainActivity, ActivityProfile::class.java))
        }
    }

    private fun sendRequest() {

        Log.d("TAG_REQ_0", "Sent")
        val email: String = binding.editText.text.toString()
        val password: String = binding.editTextTextPassword.text.toString()

        Log.d("TAG_REQ_EMAIL", email)

        val requestBody = RequestModel(email, password)

        Fuel.post(" https://dev-api.ringapp.me/auth")
            .appendHeader("Content-Type", "application/json")
            .jsonBody(requestBody)
            .response { request, response, result ->
                Log.d("TAG_REQ_2", response.statusCode.toString())

                Log.d("TAG_REQ_2", response.toString())

                Log.d("TAG_REQ_3", result.toString())

                if (response.statusCode == 200) {
                    val responseObjectType =
                        object : TypeToken<ResponseList>() {}.type
                    val responseObject = Gson().fromJson(
                        response.body().asString("application/json; charset=utf-8"),
                        responseObjectType
                    ) as? ResponseList

                    val tokenLast = responseObject?.token
                    Log.d("TAG_REQ_6", tokenLast.toString())
                    tokenForProfile = responseObject?.token
                    if (responseObject?.token != null) {
                        if (response.isSuccessful) {
                            Log.d("TAG_REQ_4", "Success")
                            Toast.makeText(
                                this,
                                "Вы успешно авторизовались",
                                Toast.LENGTH_LONG
                            ).show()

                            profileActivityIntent()

                        } else if (response.isClientError || response.isServerError) {
                            Toast.makeText(
                                this,
                                "Ошибка: ${response.statusCode}, Проверьте интернет",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("TAG_REQ_5", "Error")
                        }
                    } else{
                        Toast.makeText(
                            this,
                            "Кажется такого пользователя не существует, неверный логин или пароль",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else if (response.statusCode == -1) {
                    Toast.makeText(
                        this,
                        "Ошибка: ${response.statusCode}, Проверьте интернет  ",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Ошибка: ${response.statusCode}, Проверьте корректность поля email",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }
    private fun profileActivityIntent() {
        val i = Intent(this, ActivityProfile::class.java)
        startActivity(i)
    }

}