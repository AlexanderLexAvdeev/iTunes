package com.regula.itunes.avdeevav.repository.favorite

import com.regula.itunes.avdeevav.repository.search.data.SearchResult

interface FavoritesStorageCallback {

    fun onResult(favorites: List<SearchResult>)
}
