package com.example.bitcoinprice

import com.google.gson.annotations.SerializedName

data class BitcoinHistoryPriceResponse(
    @SerializedName("values")
    val listOfValues: List<BitcoinPriceHistoryDto>
)

data class BitcoinPriceHistoryDto(
    @SerializedName("y")
    val value: Double,
    @SerializedName("x")
    val timestamp: Long
)