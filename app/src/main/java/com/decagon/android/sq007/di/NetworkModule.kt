package com.decagon.android.sq007.di

import com.decagon.android.sq007.data.remote.PokeWilApi
import com.decagon.android.sq007.repository.PokemonRepository
import com.decagon.android.sq007.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeWilApi
    ) = PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokeWilApi(): PokeWilApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeWilApi::class.java)
    }
}
