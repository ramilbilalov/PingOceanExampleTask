package com.bilalov.task.data.request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UrlItem {

    private var retrofit: Retrofit? = null
    private val url: String = "https://like-sub.ru/items_url/"

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
}
