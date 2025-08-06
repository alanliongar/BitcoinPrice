package com.example.bitcoinprice.common.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class FilterOptions(
    val filterOptions: MutableList<filter> = mutableListOf(
        filter(filterText = "1d", days = "1days"),
        filter(filterText = "7d", days = "7days"),
        filter(filterText = "30d", days = "30days"),
        filter(filterText = "90d", days = "90days"),
        filter(filterText = "1y", days = "365days")
    )
)

data class filter(
    var filterText: String = "",
    var days: String = "",
    var isSelected: MutableState<Boolean> = mutableStateOf(false)
)