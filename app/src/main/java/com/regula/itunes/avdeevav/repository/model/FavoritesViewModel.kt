package com.regula.itunes.avdeevav.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.regula.itunes.avdeevav.repository.data.SearchResult


class FavoritesViewModel : ViewModel() {

    private var resultList: MutableLiveData<List<SearchResult>> = MutableLiveData()


    fun getFavoritesListObservable() = resultList

    fun getFavorites() {

        //
    }

    fun addToFavirites(searchResult: SearchResult) {

        //
    }
}
