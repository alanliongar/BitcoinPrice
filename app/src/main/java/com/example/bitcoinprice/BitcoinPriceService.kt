package com.example.bitcoinprice

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BitcoinPriceService {
    @GET("market-price")
    suspend fun getBitcoinPrices(
        @Query("format") format: String = "json",
        @Query("timespan") time: String
    ): Result<BitcoinPriceResponse>
}