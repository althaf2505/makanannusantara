package com.example.makanannusantara.model

import androidx.annotation.DrawableRes

data class Food(
    val nama: String,
    val deskripsi: String,
    val harga: Int,
    val asal: String,
    @DrawableRes val imageRes: Int
)