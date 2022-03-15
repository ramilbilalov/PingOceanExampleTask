package com.test.testapplicationpingocean

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isClientError
import com.github.kittinunf.fuel.core.isServerError
import com.github.kittinunf.fuel.core.isSuccessful
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.testapplicationpingocean.databinding.ActivityProfileBinding
import com.test.testapplicationpingocean.request.ResponseListProfile
import com.test.testapplicationpingocean.settingsStorage.SettingCheckerStorageImpl

lateinit var bindingSec: ActivityProfileBinding

class ActivityProfile:AppCompatActivity() {
    private val storage = SettingCheckerStorageImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSec = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(bindingSec.root)
        storage.setChecked(newValue = true)
        storage.saveChanges()
        storage.init(applicationContext)

        val sharedPreferences:SharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val saveCount: Int = sharedPreferences.getInt("count",0)
        if (saveCount==0){
            Log.e("TTT", saveCount.toString())
            defaultPreview()
        }else{
            loadData()
        }

        bindingSec.button2.setOnClickListener {
            voidActivityMain()
        }
    }


    @SuppressLint("SetTextI18n")
    fun defaultPreview() {
        Fuel.get("https://dev-api.ringapp.me/profile")
            .appendHeader("Authorization", "Bearer" + tokenForProfile.toString())
            .response {request, response, result ->
                Log.d("TAG_REQ_1", request.toString())
                Log.d("TAG_REQ_2", response.toString())
                Log.d("TAG_REQ_3", result.toString())
                var count = 0
                count ++
                val responseObjectType =
                    object : TypeToken<ResponseListProfile>() {}.type
                val responseObject = Gson().fromJson(
                    response.body().asString("application/json; charset=utf-8"),
                    responseObjectType
                ) as? ResponseListProfile
                if (response.isSuccessful) {

                    val id = responseObject?.id.toString()
                    val name = responseObject?.name.toString()
                    val email = responseObject?.email.toString()
                    val phone = responseObject?.phone.toString()
                    val avatar = responseObject?.avatar.toString()
                    val position = responseObject?.position.toString()
                    val companyName = responseObject?.company_name.toString()
                    val nameEng = responseObject?.name_eng.toString()
                    val timeZone = responseObject?.timezone.toString()

                    val sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.clear().apply{
                        putString("id", id)
                        putString("name", name)
                        putString("email", email)
                        putString("phone", phone)
                        putString("avatar", avatar)
                        putString("position", position)
                        putString("companyName", companyName)
                        putString("nameEng", nameEng)
                        putString("timeZone", timeZone)
                        putInt("count", count)
                    }.apply()

                    loadData()
                } else if (response.isClientError || response.isServerError) {
                    Toast.makeText(
                        this,
                        "Ошибка, Проверьте интернет",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("TAG_REQ_5", "Error")
                }
            }
    }
    @SuppressLint("SetTextI18n")
    private fun loadData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val  savedId: String? = sharedPreferences.getString("id", "")
        val  savedName: String? = sharedPreferences.getString("name","null")
        val  savedEmail: String? = sharedPreferences.getString("email","null")
        val  savedPhone: String? = sharedPreferences.getString("phone","null")
        val  savedAvatar: String? = sharedPreferences.getString("avatar","null")
        val  savedPosition: String? = sharedPreferences.getString("position","null")
        val  savedCompanyName: String? = sharedPreferences.getString("companyName","null")
        val  savedNameEng: String? = sharedPreferences.getString("nameEng","null")
        val  savedTimeZone: String? = sharedPreferences.getString("timeZone","null")

        bindingSec.textView.text = "Мой id: $savedId"
        bindingSec.textView2.text = "Мое имя: $savedName"
        bindingSec.textView3.text = "Моя почта: $savedEmail"
        bindingSec.textView4.text = "Мой номер телефона: $savedPhone"
        bindingSec.textView5.text = "Мой Аватар: $savedAvatar"
        bindingSec.textView6.text = "Моя позиция: $savedPosition"
        bindingSec.textView7.text = "Имя компании: $savedCompanyName"
        bindingSec.textView8.text = "Имя на анлийском: $savedNameEng"
        bindingSec.textView9.text = "Мой часовой пояс: $savedTimeZone"
    }
    private fun voidActivityMain(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}