package com.example.bitcoinprice.history

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_HIST_URL: String = "https://api.blockchain.info/charts/"


object BitHistRetrofitClient {
    val retrofitInstance: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_HIST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}

