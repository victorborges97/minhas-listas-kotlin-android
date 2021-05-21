package com.borges.minhaslistas.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataList (
    var idList: String? = null,
    val nomeDaLista: String? = null,
    val created: String? = null
) : Parcelable

