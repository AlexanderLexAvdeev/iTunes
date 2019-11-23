package com.regula.itunes.avdeevav.repository.favorite

import com.regula.itunes.avdeevav.repository.data.SearchResult

interface FavoritesStorageCallback {

    fun onResult(favorites: List<SearchResult>)
}
