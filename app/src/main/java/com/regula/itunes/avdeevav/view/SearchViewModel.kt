package com.regula.itunes.avdeevav.view

import android.os.Bundle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

import com.regula.itunes.avdeevav.repository.data.SearchResult
import com.regula.itunes.avdeevav.repository.loader.ErrorCallback
import com.regula.itunes.avdeevav.repository.loader.SearchResultLoader


class SearchViewModel : ViewModel(), LoaderManager.LoaderCallbacks<List<SearchResult>>, ErrorCallback {

    private companion object {
        const val SEARCH_RESULT_LOADER = 1
    }

    private lateinit var query: String
    private lateinit var mediaType: String

    private lateinit var loaderManager: LoaderManager
    private var resultList: MutableLiveData<List<SearchResult>> = MutableLiveData()


    fun getResultListObservable(loaderManager: LoaderManager): MutableLiveData<List<SearchResult>> {

        this.loaderManager = loaderManager

        return resultList
    }

    fun requestResult(query: String, mediaType: String) {

        this.query = query
        this.mediaType = mediaType

        loaderManager.restartLoader(SEARCH_RESULT_LOADER, null, this@SearchViewModel)
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<SearchResult>> {

        return SearchResultLoader(query, mediaType, this@SearchViewModel)
    }

    override fun onLoadFinished(loader: Loader<List<SearchResult>>, data: List<SearchResult>?) {

        when (loader.id) {
            SEARCH_RESULT_LOADER -> resultList.value = data
        }
    }

    override fun onLoaderReset(loader: Loader<List<SearchResult>>) {
    }

    override fun onError(message: String) {
    }
}
