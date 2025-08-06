package com.example.bitcoinprice

import com.google.gson.annotations.SerializedName

data class BitcoinPriceDto(
    @SerializedName("USD")
    val usd: currency,
){
    data class currency(
        @SerializedName("15m") val fifMin : Double,
        @SerializedName("last") val last: Double,
        @SerializedName("buy") val buy: Double,
        @SerializedName("sell") val sell: Double,
        @SerializedName("symbol")  val symbol: String,
    )

}
