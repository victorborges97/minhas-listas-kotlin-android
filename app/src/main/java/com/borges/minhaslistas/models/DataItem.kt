package com.borges.minhaslistas.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataItem (
    var idItem: String? = null,
    var comprado: Boolean? = false,
    var nome: String? = null,
    var preco: Double? = 0.0,
    var qt: Int? = 0,
    var total: Double? = 0.0,
    var idList: String? = null
) : Parcelable