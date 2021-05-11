package com.decagon.android.sq007.util

import com.decagon.android.sq007.data.model.PokeWilListModel

object PokemonListUtil {
    var localPokemonList: MutableList<PokeWilListModel> = ArrayList()

    fun addPokemon(item: PokeWilListModel) {
        localPokemonList.add(item)
    }

    fun deletePokemon(item: PokeWilListModel) {
        localPokemonList.remove(item)
    }

    fun getPokemonList(): List<PokeWilListModel> {
        return localPokemonList
    }
}
