package com.borges.minhaslistas.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataList (
    var idList: String? = null,
    val nomeDaLista: String? = null,
    val nomeDoMercado: String? = null,
    val timestamp: Timestamp? = null,
    val created: String? = null
) : Parcelable

