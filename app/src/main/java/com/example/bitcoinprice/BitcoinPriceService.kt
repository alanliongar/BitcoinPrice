package com.example.bitcoinprice

import retrofit2.http.GET

interface BitcoinPriceService {
    @GET("ticker")
    suspend fun getBitcoinPrices(): BitcoinPriceDto
}