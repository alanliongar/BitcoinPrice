package com.example.bitcoinprice

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BitcoinPriceService {
    @GET("{time}days")
    suspend fun getBitcoinPrices(@Path("time") time: Int): Result<BitcoinPriceDto>
}