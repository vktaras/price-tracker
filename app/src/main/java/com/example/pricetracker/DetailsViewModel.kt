package com.example.pricetracker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val symbol: String = savedStateHandle["symbol"] ?: ""

}
