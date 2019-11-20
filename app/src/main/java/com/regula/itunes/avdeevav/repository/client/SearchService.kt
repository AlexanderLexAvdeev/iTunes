package com.regula.itunes.avdeevav.repository.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import com.regula.itunes.avdeevav.repository.data.SearchResults

interface SearchService {

    @GET("/search")
    fun getResult(
            @Query("term") term: String,
            @Query("media") media: String
    ): Call<SearchResults>
}