package com.example.bitcoinprice

import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitcoinprice.common.utils.FilterOptions
import com.example.bitcoinprice.common.model.BitcoinPriceDto
import com.example.bitcoinprice.history.BitHistRetrofitClient
import com.example.bitcoinprice.history.model.BitcoinHistoryPriceResponse
import com.example.bitcoinprice.history.BitcoinHistoryPriceService
import com.example.bitcoinprice.history.model.BitcoinPriceHistoryDto
import com.example.bitcoinprice.price.data.remote.BitPriceRetrofitClient
import com.example.bitcoinprice.price.data.remote.BitcoinPriceService
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Line
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun BitcoinPriceScreen(modifier: Modifier = Modifier) {
    var filter = remember { mutableStateOf<String>("") }
    var historyPrices = remember { mutableStateOf<BitcoinHistoryPriceResponse?>(null) }
    var nowPrice = remember { mutableStateOf<BitcoinPriceDto?>(null) }
    var exCeption = remember { mutableStateOf<Exception?>(null) }
    //Achei melhor tratar o estado de erro assim, jogando ele pra uma variável e mudando o estado dele
    BitcoinPriceContent(
        filter = filter,
        historyPrices = historyPrices,
        nowPrice = nowPrice,
        exCeption = exCeption
    )
}


@Composable
private fun BitcoinPriceContent(
    filter: MutableState<String>,
    historyPrices: MutableState<BitcoinHistoryPriceResponse?>,
    nowPrice: MutableState<BitcoinPriceDto?>, exCeption: MutableState<Exception?>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Bitcoin Price (USD)",
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF16B919)
        )
        BitcoinPricesAndFiltersRequests(
            filter = filter,
            historyPrices = historyPrices,
            nowPrice = nowPrice,
            exCeption = exCeption
        )
        BitcoinPriceAPIRequest(
            nowPrice = nowPrice,
            scope = rememberCoroutineScope(),
            exCeption = exCeption
        )
        if (exCeption.value == null) {
            Spacer(modifier = Modifier.size(20.dp))
            historyPrices.value?.listOfValues?.let { MainGraph(it) }
            Spacer(modifier = Modifier.size(40.dp))
            BitcoinPrice(nowPrice = nowPrice)

        } else {
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                "Something went wrong. Try filtering again!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}

@Composable
fun BitcoinPrice(modifier: Modifier = Modifier, nowPrice: MutableState<BitcoinPriceDto?>) {
    var color = Color.Black
    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Last Trade Price:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(10.dp))
        if (nowPrice.value != null) {
            val startPrice = nowPrice.value?.usd?.fifMin
            val endPrice = nowPrice.value?.usd?.last
            val priceVariation = if (startPrice != null && endPrice != null) {
                (endPrice - startPrice) / startPrice
            } else {
                0.0
            }
            if (priceVariation < 0) {
                //Se a variação for menor que 0, significa que caiu.
                color = Color.Red
            } else {
                color = Color(0xFF16B919)
            }

            Text(
                text = "$${nowPrice.value?.usd?.last.toString()}",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold, color = color
            )
        } else {
            Text(text = "Loading...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}


private fun BitcoinPriceAPIRequest(
    nowPrice: MutableState<BitcoinPriceDto?>,
    scope: CoroutineScope, exCeption: MutableState<Exception?>
) {
    val bitcoinPriceApiService =
        BitPriceRetrofitClient.retrofitInstance.create(BitcoinPriceService::class.java)
    scope.launch {
        try {
            val response = bitcoinPriceApiService.getBitcoinPrices()
            nowPrice.value = response
            exCeption.value = null
        } catch (ex: Exception) {
            exCeption.value = ex
            ex.printStackTrace()
            Log.e("API Error", ex.message ?: "Unknown error")
        }
    }
}


@Composable
private fun BitcoinPricesAndFiltersRequests(
    filter: MutableState<String>,
    historyPrices: MutableState<BitcoinHistoryPriceResponse?>,
    nowPrice: MutableState<BitcoinPriceDto?>, exCeption: MutableState<Exception?>
) {
    Column(modifier = Modifier.wrapContentHeight()) {
        Spacer(modifier = Modifier.size(20.dp))
        FilterGrid(filtros = FilterOptions(), filter = filter)
        Spacer(modifier = Modifier.size(20.dp))
        BitcoinPricesApisRequests(
            filter = filter,
            valoresTeste = historyPrices,
            nowPrice = nowPrice, exCeption = exCeption
        )
    }
}

@Composable
private fun BitcoinPricesApisRequests(
    valoresTeste: MutableState<BitcoinHistoryPriceResponse?>,
    filter: MutableState<String>,
    nowPrice: MutableState<BitcoinPriceDto?>,
    modifier: Modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth(), exCeption: MutableState<Exception?>
) {
    val bitcoinHistoryPriceApiService =
        BitHistRetrofitClient.retrofitInstance.create(BitcoinHistoryPriceService::class.java)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(filter.value) {
        BitcoinPriceAPIRequest(nowPrice = nowPrice, coroutineScope, exCeption = exCeption)
        //Coloquei essa chamada aqui, pois o preço do bitcoin sempre atualiza junto
        delay(50) //achei mais seguro dar um tempinho entre uma corrotina e outra
        coroutineScope.launch {
            if (filter.value != "") {
                try {
                    val response =
                        bitcoinHistoryPriceApiService.getBitcoinHistoryPrices(time = filter.value)
                    valoresTeste.value = response
                    exCeption.value = null
                } catch (ex: Exception) {
                    exCeption.value = ex
                    Log.e("API Error", ex.message ?: "Unknown error")
                }
            } else {

            }
        }
    }



    Column {
        val listSize = valoresTeste.value?.listOfValues?.size
        if (valoresTeste == null) {
            Text(
                text = "Loading...", //estado de loading simples
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        } else if (listSize == null) {
            Text(
                text = "Please, select a filter",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFF14D1DD)
            )
        } else {
            /*Text( //Usei essa parte só pra ver se a chamada tava empilhando tudo certinho
                text = "Tamanho da lista: $listSize",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )*/
        }
    }
}


@Composable
private fun MainGraph(list: List<BitcoinPriceHistoryDto>) {
    Box(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        GraphDesignItem(list = list)
    }
}


@Composable
private fun GraphDesignItem(list: List<BitcoinPriceHistoryDto>) {
    val lista = list.map {
        it.value
    }

    val maxValue = lista.maxOrNull() ?: 0.0
    val minValue = lista.minOrNull() ?: 0.0

    LineChart(
        //esse elemento aqui é de uma biblioteca aleatória que achei buscando no github, nem achei documentação pra ela e perdi mto tempo aprendendo como funciona
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
        data =
            listOf(
                Line(
                    label = "Bitcoin Price",
                    values = lista,
                    color = SolidColor(randomColor()),
                    firstGradientFillColor = randomColor(),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 100,
                )
            ),
        animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
        minValue = minValue * 0.995, //variação de 0,5% abaixo para o mínimo
        maxValue = maxValue * 1.005, //variação de 0,5% acima para o máximo
    )
}

@Composable
private fun FilterGrid(
    filter: MutableState<String>,
    filtros: FilterOptions,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        contentPadding = PaddingValues(0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(filtros.filterOptions.size) { index ->
            FilterButton(
                onClick = {
                    filtros.filterOptions.forEach { it.isSelected.value = false }
                    filtros.filterOptions[index].isSelected.value =
                        !filtros.filterOptions[index].isSelected.value
                    filter.value = filtros.filterOptions[index].days
                },
                isSelected = filtros.filterOptions[index].isSelected,
                text = filtros.filterOptions[index].filterText
            )
        }
    }
}

@Composable
private fun FilterButton(
    isSelected: MutableState<Boolean>,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier.wrapContentHeight(),
) {
    if (isSelected.value) {
        Button(
            enabled = true,
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(0.dp)
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue.copy(alpha = 0.5f),
                contentColor = Color.White.copy(alpha = 1.0f)
            )
        ) {
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    } else {
        Button(
            enabled = true,
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(0.dp)
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA500).copy(alpha = 0.5f),
                contentColor = Color.White.copy(alpha = 1.0f)
            )
        ) {
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

private fun randomColor(): Color {
    return Color( //Apesar de não ser composable, decidi deixar dentro do arquivo screen pq serve pra auxiliar ele.
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

private fun convertTimestampToDate(timestamp: Long): String {
    // Cria uma instância de Date a partir do timestamp
    val date =
        //Apesar de não ser composable, decidi deixar dentro do arquivo screen pq serve pra auxiliar ele.
        Date(timestamp * 1000) // O timestamp geralmente está em segundos, então multiplicamos por 1000

    // Define o formato desejado para a data
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    // Retorna a data formatada como uma String
    return dateFormat.format(date)
}
//sem preview dessa vez