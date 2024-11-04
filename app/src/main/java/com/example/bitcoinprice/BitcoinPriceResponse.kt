package com.example.bitcoinprice

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class BitcoinPriceResponse(
    @SerializedName("values")
    val listOfValues: List<BitcoinPriceDto>
)

@kotlinx.serialization.Serializable
data class BitcoinPriceDto(
    @SerializedName("x")
    val timestamp: Long,
    @SerializedName("y")
    val value: Double
)

/*
// Classe principal para o retorno da API
@kotlinx.serialization.Serializable
data class BitcoinPriceResponse(
    val status: String,
    val name: String,
    val unit: String,
    val period: String,
    val description: String,
    val values: List<PriceValue>
)

// Classe para representar os valores de preço no JSON
@kotlinx.serialization.Serializable
data class PriceValue(
    val x: Long,  // Usando Long para representar o timestamp
    val y: Double // Usando Double para representar o preço
)*/
