package com.borges.minhaslistas.model

data class DataItem (
    var idItem: String? = null,
    var comprado: Boolean? = false,
    var nome: String? = null,
    var preco: Double? = 0.0,
    var qt: Int? = 0,
    var total: Double? = 0.0
)