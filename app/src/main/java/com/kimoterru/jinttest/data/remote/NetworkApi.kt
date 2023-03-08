package com.kimoterru.jinttest.data.remote

import com.kimoterru.jinttest.data.remote.model.BinResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    @GET("{bin}")
    suspend fun getBINBankCard(
        @Path("bin") bin: Int
    ): BinResponse

}