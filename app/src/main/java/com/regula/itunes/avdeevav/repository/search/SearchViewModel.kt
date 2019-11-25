package com.regula.itunes.avdeevav.repository.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.regula.itunes.avdeevav.repository.search.data.SearchResult
import com.regula.itunes.avdeevav.repository.search.loader.SearchResultLoader
import com.regula.itunes.avdeevav.repository.search.loader.SearchResultLoaderCallback
import com.regula.itunes.avdeevav.view.search.ISearchActivity

class SearchViewModel : ViewModel(), SearchResultLoaderCallback {

    private lateinit var query: String
    private lateinit var mediaType: String

    private lateinit var iSearchActivity: ISearchActivity
    private var resultList: MutableLiveData<List<SearchResult>> = MutableLiveData()


    fun getResultListObservable(iSearchActivity: ISearchActivity): MutableLiveData<List<SearchResult>> {

        this.iSearchActivity = iSearchActivity

        return resultList
    }

    fun requestResult(query: String, mediaType: String) {

        this.query = query
        this.mediaType = mediaType

        SearchResultLoader(
                SearchQueryFormatter.getFormattedRequest(query),
                mediaType,
                this@SearchViewModel
        ).start()
    }


    // SearchResultLoaderCallback

    override fun onResult(searchResults: List<SearchResult>?) {

        resultList.value = searchResults
    }

    override fun onError(message: String) {

        iSearchActivity.showError(message)
    }
}
