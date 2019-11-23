package com.regula.itunes.avdeevav.repository.search.web

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import com.regula.itunes.avdeevav.repository.search.data.SearchResults

interface SearchService {

    @GET("/search")
    fun getResult(
            @Query("term") term: String,
            @Query("media") media: String
    ): Call<SearchResults>
}