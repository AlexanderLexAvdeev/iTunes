package com.regula.itunes.avdeevav.repository

import com.regula.itunes.avdeevav.repository.data.SearchResult

interface FavoritesCallback {

    fun onResult(favorites: List<SearchResult>)
}