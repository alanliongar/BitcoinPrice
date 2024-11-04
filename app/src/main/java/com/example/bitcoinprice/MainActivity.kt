package com.example.bitcoinprice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitcoinprice.ui.theme.BitcoinPriceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val filtros = FilterOptions()
        setContent {
            BitcoinPriceTheme {
                val bitcoinApiService =
                    BitRetrofitClient.retrofitInstance.create(BitcoinPriceService::class.java)
                //var valoresTeste by remember { mutableStateOf<Result<BitcoinPriceResponse>?>(null) }
                var valoresTeste = remember { mutableStateOf<Result<BitcoinPriceResponse>?>(null) }
                LaunchedEffect(Unit) {
                    val response = bitcoinApiService.getBitcoinPrices(time = "3days")
                    valoresTeste.value = if (response.isSuccess) {
                        Log.d("Alannn", response.toString())
                        response
                    } else {
                        Result.failure(Exception("Failed to fetch data"))
                    }
                }

                Column {
                    if (valoresTeste.value == null) {
                        Text(text = "Carregando...") // Exibe um texto de carregamento enquanto a API responde
                    } else {
                        val listSize = valoresTeste.value?.getOrNull()?.listOfValues?.size ?: 0
                        Text(text = "Tamanho da lista: $listSize") // Exibe o tamanho da lista após o carregamento
                    }
                }

                /*Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FilterGrid(filtros)
                    Spacer(modifier = Modifier.padding(0.dp))
                }*/

            }
        }
    }
}


@Composable
fun FilterGrid(filtros: FilterOptions, modifier: Modifier = Modifier) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(filtros.filterOptions.size) { index ->
            FilterButton(
                onClick = {
                    filtros.filterOptions.forEach { it.isSelected.value = false }
                    filtros.filterOptions[index].isSelected.value =
                        !filtros.filterOptions[index].isSelected.value
                },
                isSelected = filtros.filterOptions[index].isSelected,
                text = filtros.filterOptions[index].text
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
