package com.regula.itunes.avdeevav.repository

data class SearchResults(
        var resultCount: Int? = 0,
        var results: ArrayList<SearchResult>? = null
)
