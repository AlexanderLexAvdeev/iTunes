package com.regula.itunes.avdeevav.view

import com.regula.itunes.avdeevav.repository.search.data.SearchResult

interface IListAdapter {

    fun onFavoritesClick(searchResult: SearchResult)
}
