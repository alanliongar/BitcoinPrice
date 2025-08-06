package com.example.bitcoinprice.price.data.remote

import com.example.bitcoinprice.common.model.BitcoinPriceDto
import retrofit2.http.GET

interface BitcoinPriceService {
    @GET("ticker")
    suspend fun getBitcoinPrices(): BitcoinPriceDto
}