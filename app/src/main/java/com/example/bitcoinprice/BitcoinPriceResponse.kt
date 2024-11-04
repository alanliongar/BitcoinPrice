package com.example.bitcoinprice

import com.google.gson.annotations.SerializedName

data class BitcoinPriceResponse(
    @SerializedName("values")
    val listOfValues: List<BitcoinPriceDto>
)

data class BitcoinPriceDto(
    @SerializedName("y")
    val value: Double
)