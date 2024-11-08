package com.example.bitcoinprice

import retrofit2.http.GET
import retrofit2.http.Query

interface BitcoinHistoryPriceService {
    @GET("market-price")
    suspend fun getBitcoinHistoryPrices(
        @Query("format") format: String = "json",
        @Query("timespan") time: String
    ): BitcoinHistoryPriceResponse
}