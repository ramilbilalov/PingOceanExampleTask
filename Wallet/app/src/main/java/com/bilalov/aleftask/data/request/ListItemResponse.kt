package com.bilalov.aleftask.data.request

import retrofit2.Call
import retrofit2.http.GET

interface ListItemResponse {
    @GET("task-m-001/list.php")
    fun getAllItems(): Call<List<String>>
}