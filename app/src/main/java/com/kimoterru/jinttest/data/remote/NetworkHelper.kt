package com.kimoterru.jinttest.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkHelper {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lookup.binlist.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var service: NetworkApi? = null

    fun getService() : NetworkApi {
        if (service == null) {
            service = retrofit.create(NetworkApi::class.java)
        }
        return service!!
    }

}