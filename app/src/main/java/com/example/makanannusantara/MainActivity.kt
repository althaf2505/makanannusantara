package com.example.makanannusantara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import com.example.makanannusantara.model.*
import com.example.makanannusantara.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakananTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔥 TITLE
        item {
            Text(
                text = "Makanan Nusantara",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // 🔍 SEARCH
        item {
            var search by remember { mutableStateOf("") }

            TextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Cari Makanan...") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 🔥 REKOMENDASI (HORIZONTAL)
        item {
            Text("Rekomendasi", fontWeight = FontWeight.Bold)

            LazyRow {
                items(FoodSource.dummyFood) { food ->
                    FoodItemHorizontal(food)
                }
            }
        }

        // 🍽️ LIST UTAMA
        items(FoodSource.dummyFood) { food ->
            FoodCard(food)
        }
    }
}

@Composable
fun FoodCard(food: Food) {

    var isFavorite by remember { mutableStateOf(false) }

    // 🔥 STATE COROUTINE
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxWidth()) {

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column {

                // 🔥 IMAGE + FAVORITE
                Box {
                    Image(
                        painter = painterResource(food.imageRes),
                        contentDescription = food.nama,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = if (isFavorite)
                                Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(food.nama, fontWeight = FontWeight.Bold)
                    Text("Asal: ${food.asal}")
                    Text(food.deskripsi)

                    Text(
                        "Rp ${food.harga}",
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🚀 BUTTON ASYNC (MODUL 9)
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(2000)

                                snackbarHostState.showSnackbar(
                                    "Pesanan ${food.nama} berhasil diproses!"
                                )

                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {

                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Memproses...")
                        } else {
                            Text("Pesan Sekarang")
                        }
                    }
                }
            }
        }

        // 🔥 SNACKBAR
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun FoodItemHorizontal(food: Food) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
    ) {
        Column {
            Image(
                painter = painterResource(food.imageRes),
                contentDescription = "",
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = food.nama,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}