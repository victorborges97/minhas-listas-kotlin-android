package com.borges.minhaslistas.model

data class DataList (
    val id: String? = null,
    val nomeDaLista: String? = null,
    val created: String? = null,
    val itens: ArrayList<DataItem>? = null
)

