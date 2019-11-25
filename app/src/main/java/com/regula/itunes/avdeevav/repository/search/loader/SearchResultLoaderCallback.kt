package com.regula.itunes.avdeevav.repository.search.loader

import com.regula.itunes.avdeevav.repository.search.data.SearchResult

interface SearchResultLoaderCallback {

    fun onResult(searchResults: List<SearchResult>?)
    fun onError(message: String)
}
