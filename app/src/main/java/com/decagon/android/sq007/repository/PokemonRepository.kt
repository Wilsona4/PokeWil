package com.decagon.android.sq007.repository

import com.decagon.android.sq007.data.remote.PokeWilApi
import com.decagon.android.sq007.data.remote.response.Pokemon
import com.decagon.android.sq007.data.remote.response.PokemonList
import com.decagon.android.sq007.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeWilApi
) {
//    Get our pokemon responses for calls made by PokeWilApi

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
//    check if network call is successful then emmit data (success) or error (failure)
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("Error Occurred")
        }

        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {

        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("Error Occurred")
        }

        return Resource.Success(response)
    }
}
