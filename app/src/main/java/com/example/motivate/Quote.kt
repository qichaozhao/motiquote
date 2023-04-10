package com.example.motivate

data class Quote(
    val id: Int,
    val quote: String,
    val font_style: String,
    val font_size: Int,
    val x_position: Int,
    val y_position: Int,
    val image_resource: String
)