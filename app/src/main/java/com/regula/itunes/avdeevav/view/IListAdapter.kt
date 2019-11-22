package com.regula.itunes.avdeevav.view

import com.regula.itunes.avdeevav.repository.data.SearchResult

interface IListAdapter {

    fun onFavoritesClick(searchResult: SearchResult)
}
