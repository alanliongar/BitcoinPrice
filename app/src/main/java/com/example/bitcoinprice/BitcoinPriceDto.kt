package com.example.bitcoinprice

import com.google.gson.annotations.SerializedName

data class BitcoinPriceDto(
    @SerializedName("price_24h")
    val firstPrice: Double,
    @SerializedName("last_trade_price")
    val lastTradePrice: Double,
){
    val priceVariation: Double
        get(){
            return lastTradePrice/firstPrice
        }
}
