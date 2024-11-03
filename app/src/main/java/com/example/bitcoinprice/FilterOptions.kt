package com.example.bitcoinprice

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class FilterOptions(
    val filterOptions: MutableList<filter> = mutableListOf(
        filter(text = "1d", number = 2),
        filter(text = "7d", number = 8),
        filter(text = "30d", number = 31),
        filter(text = "90d", number = 91),
        filter(text = "1y", number = 366)
    )
)

data class filter(
    var text: String = "",
    var number: Int = 0,
    var isSelected: MutableState<Boolean> = mutableStateOf(false)
)