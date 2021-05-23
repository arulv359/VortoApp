package com.codingassessment.vortoarul.net

import com.codingassessment.vortoarul.model.BusinessesList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("search")
    fun loadBusiness(@Query("term") term: String, @Query("latitude") latitude: String, @Query("longitude") longitude: String): Call<BusinessesList>
}