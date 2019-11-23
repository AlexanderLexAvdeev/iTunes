package com.regula.itunes.avdeevav.repository.web

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpClient {

    private const val url = "https://itunes.apple.com"

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getSearchService(): SearchService {

        return retrofit.create(SearchService::class.java)
    }
}