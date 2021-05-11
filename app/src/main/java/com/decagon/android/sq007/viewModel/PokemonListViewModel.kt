package com.decagon.android.sq007.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decagon.android.sq007.data.model.PokeWilListModel
import com.decagon.android.sq007.repository.PokemonRepository
import com.decagon.android.sq007.util.Constant.PAGE_SIZE
import com.decagon.android.sq007.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0
    private var _pokemonList = MutableLiveData<List<PokeWilListModel>>(listOf())
    val pokemonList: LiveData<List<PokeWilListModel>> get() = _pokemonList

    var loadError = MutableStateFlow("")
    var isLoading = MutableStateFlow(false)
    var endReached = MutableStateFlow(false)

    init {
        loadPaginatedPokemonList()
    }

    /* Load Paginated Pokemon Page i.e. 20 at a time*/
    fun loadPaginatedPokemonList() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)) {
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    /*Map each loaded List Entry*/
                    val pokeWilEntries = result.data.results.mapIndexed { index, entry ->
                        /*Get each Pokemon number*/
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        /*Get Corresponding Pokemon Image*/
                        val url = "https://pokeres.bastionbot.org/images/pokemon/$number.png"
//                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"
                        PokeWilListModel(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    currentPage++

                    loadError.value = ""
                    isLoading.value = false
                    _pokemonList.value = pokemonList.value?.plus(pokeWilEntries)
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
}
