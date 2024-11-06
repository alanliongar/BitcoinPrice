package com.example.bitcoinprice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Line
import kotlinx.coroutines.launch
import kotlin.random.Random

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val filtros = FilterOptions()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {

            var filter = remember { mutableStateOf<String>("") }
            var valoresTeste = remember { mutableStateOf<BitcoinPriceResponse?>(null) }
            Column(modifier = Modifier.fillMaxSize()) {
                BitcoinPriceFiltersAndRequest(filter = filter, valoresTeste = valoresTeste)
                Spacer(modifier = Modifier.size(20.dp))
                valoresTeste.value?.listOfValues?.let { TelaPrincipal(it) }

            }
        }
    }
}


@Composable
fun BitcoinPriceFiltersAndRequest(
    filter: MutableState<String>,
    valoresTeste: MutableState<BitcoinPriceResponse?>
) {
    Column {
        Spacer(modifier = Modifier.size(20.dp))
        FilterGrid(filtros = filtros, filter = filter)
        Spacer(modifier = Modifier.size(20.dp))
        BitcoinApiRequest(filter = filter, valoresTeste = valoresTeste)
    }
}

@Composable
fun BitcoinApiRequest(
    valoresTeste: MutableState<BitcoinPriceResponse?>,
    filter: MutableState<String>,
    modifier: Modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
) {
    val bitcoinApiService =
        BitRetrofitClient.retrofitInstance.create(BitcoinPriceService::class.java)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(filter.value) {
        coroutineScope.launch {
            try {
                val response = bitcoinApiService.getBitcoinPrices(time = filter.value)
                valoresTeste.value = response
                Log.d("API Response", response.toString())
            } catch (e: Exception) {
                Log.e("API Error", e.message ?: "Unknown error")
            }
        }
    }

    Column {
        val listSize = valoresTeste.value?.listOfValues?.size
        if (valoresTeste == null) {
            Text(
                text = "Carregando...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ) //Exibe um texto de carregamento enquanto a API responde

        } else if (listSize == null) {
            Text(
                text = "Selecione um filtro!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = "Tamanho da lista: $listSize",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TelaPrincipal(list: List<BitcoinPriceDto>) {
    Box(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Graph(list = list)
    }
}


@Composable
fun Graph(list: List<BitcoinPriceDto>) {
    val lista = list.map {
        it.value
    }

    val maxValue = lista.maxOrNull() ?: 0.0
    val minValue = lista.minOrNull() ?: 0.0
    Log.d("Alannn", "O valor máximo é: $maxValue e o mínimo é: $minValue")


    LineChart(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
        data =
        listOf(
            Line(
                label = "Windows",
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


fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}


fun convertTimestampToDate(timestamp: Long): String {
    // Cria uma instância de Date a partir do timestamp
    val date =
        Date(timestamp * 1000) // O timestamp geralmente está em segundos, então multiplicamos por 1000

    // Define o formato desejado para a data
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    // Retorna a data formatada como uma String
    return dateFormat.format(date)
}

@Composable
fun FilterGrid(
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
fun FilterButton(
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
