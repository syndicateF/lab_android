package com.fajrin222280062.kelas_c.kelascti2022

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

     private const val BASE_URL = "http://10.0.2.2:8080/"
//    private const val BASE_URL = "http://192.168.1.6:8080/"

    private var retrofit: Retrofit? = null

    fun getApiClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}