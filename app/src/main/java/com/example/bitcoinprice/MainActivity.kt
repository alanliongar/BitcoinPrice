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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
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
        if (valoresTeste == null) {
            Text(text = "Carregando...") // Exibe um texto de carregamento enquanto a API responde
        } else {
            val listSize = valoresTeste.value?.listOfValues?.size
            Text(
                text = "Tamanho da lista: $listSize",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ) // Exibe o tamanho da lista após o carregamento
        }
    }
}

@Composable
fun TelaPrincipal(list: List<BitcoinPriceDto>) {
    Box(
        modifier = Modifier
            .height(400.dp).fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        MeuGrafico(list = list)
    }
}


@Composable
fun MeuGrafico(list: List<BitcoinPriceDto>) {
    // Configura o gráfico dentro do composable
    val lista = list.map {
        it.value
    }
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
                secondGradientFillColor = randomColor(),
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 100,
            )
        ),
        animationMode = AnimationMode.Together(delayBuilder = { it * 500L })
    )
}

/*@Composable
fun LineChartSample(listOfObjects: List<BitcoinPriceDto?>) {
    var list: List<Double>
    if (listOfObjects.isNotEmpty()) {
        list = listOfObjects.map {
            it?.value ?: 0.0
        }
    } else {
        list = emptyList()
    }
    val xAxisData = list.mapIndexed { index, _ -> "$index" }
    Log.d("Alannn", xAxisData.size.toString())

    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "BitcoinPrice",
            data = list,
            lineColor = randomColor(),
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        ),
        *//*LineParameters(
            label = "Earnings",
            data = listOf(60.0, 80.6, 40.33, 86.232, 88.0, 90.0),
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Earnings",
            data = listOf(1.0, 40.0, 11.33, 55.23, 1.0, 100.0),
            lineColor = Color(0xFF81BE88),
            lineType = LineType.CURVED_LINE,
            lineShadow = false,
        )*//*
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        LineChart(
            modifier = Modifier.fillMaxWidth(),
            linesParameters = testLineParameters,
            isGrid = true,
            gridColor = Color.Blue,
            xAxisData = xAxisData,
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = randomColor(),
            ),
            xAxisStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 15,
            oneLineChart = false,
            gridOrientation = GridOrientation.VERTICAL
        )
    }
}*/


fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f // Pode ajustar a opacidade se necessário
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


fun randomLength(): Float {
    return Random.nextFloat() * 100f // Gera um valor entre 0 e 100
}

/*fun BitcoinApiValuesRequest(
    time: String,
    bitcoinApiService: BitcoinPriceService
): Result<BitcoinPriceResponse?> {
    GlobalScope.launch(Dispatchers.IO) {
        val finalResult: Result<BitcoinPriceResponse?>
        try {
            val response = bitcoinApiService.getBitcoinPrices(time = time)
            if (response.isSuccess) {
                finalResult = response
            } else {

            }
            Log.d("API Response", response.toString())
        } catch (e: Exception) {
            Log.e("API Error", e.message ?: "Unknown error")
        }
    }
}*/




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
