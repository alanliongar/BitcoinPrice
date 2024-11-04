package com.example.bitcoinprice

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class BitcoinPriceDto(
    @SerializedName("values")
    val listOfValues: List<PriceAndDateValue>
)

data class PriceAndDateValue(
    @SerializedName("y")
    val value: Int
)