package com.decagon.android.sq007.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokeWilListModel(
    val pokemonName: String,
    val imageUrl: String,
    val number: Int
) : Parcelable
