package com.example.bitcoinprice

import retrofit2.http.GET

interface BitcoinPriceService {
    @GET("tickers/BTC-USD")
    suspend fun getBitcoinPrices(): BitcoinPriceDto
}