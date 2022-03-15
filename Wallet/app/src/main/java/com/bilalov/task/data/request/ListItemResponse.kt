package com.bilalov.task.data.request

import retrofit2.Call
import retrofit2.http.GET

interface ListItemResponse {
    @GET("/items.json")
    fun getAllItems(): Call<List<String>>
}