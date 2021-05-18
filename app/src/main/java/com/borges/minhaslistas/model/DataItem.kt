package com.borges.minhaslistas.model

data class DataItem (
    val preco: Double? = 0.0,
    val total: Double? = 0.0,
    val qt: Int? = 0,
    val nome: String? = null,
    val comprado: Boolean? = false
)