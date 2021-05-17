package com.borges.minhaslistas.model

data class DataList (
    val id: String? = null,
    val nome: String? = null,
    val created: String? = null,
    val itens: ArrayList<Item>? = null
)

