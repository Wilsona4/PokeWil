package com.decagon.android.sq007.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decagon.android.sq007.data.remote.response.Pokemon
import com.decagon.android.sq007.repository.PokemonRepository
import com.decagon.android.sq007.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var _loadError = MutableLiveData<String>()
    val loadError: LiveData<String> get() = _loadError

    private var _myPokemonInfo = MutableLiveData<Pokemon>()
    val myPokemonInfo: LiveData<Pokemon> get() = _myPokemonInfo

    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            when (val result = repository.getPokemonInfo(pokemonName)) {
                is Resource.Success -> {
                    _myPokemonInfo.value = result.data
                    _loadError.value = ""
                }
                else -> {
                    _loadError.value = result.message!!
                }
            }
        }
    }
}
