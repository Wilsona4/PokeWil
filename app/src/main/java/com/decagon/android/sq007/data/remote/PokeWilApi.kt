package com.decagon.android.sq007.data.remote

import com.decagon.android.sq007.data.remote.response.Pokemon
import com.decagon.android.sq007.data.remote.response.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeWilApi {

//  outline all Sub-urls which data would be retrieved from

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon
}
