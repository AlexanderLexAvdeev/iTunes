package com.regula.itunes.avdeevav.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.regula.itunes.avdeevav.repository.SearchQueryFormatter

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
    private lateinit var iSearchActivity: ISearchActivity
    private var resultList: MutableLiveData<List<SearchResult>> = MutableLiveData()


    fun getResultListObservable(
        loaderManager: LoaderManager,
        iSearchActivity: ISearchActivity
    ): MutableLiveData<List<SearchResult>> {

        this.loaderManager = loaderManager
        this.iSearchActivity = iSearchActivity

        return resultList
    }

    fun requestResult(query: String, mediaType: String) {

        this.query = query
        this.mediaType = mediaType

        loaderManager.restartLoader(SEARCH_RESULT_LOADER, null, this@SearchViewModel)
    }


    // LoaderCallbacks
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<SearchResult>> {

        return SearchResultLoader(
            SearchQueryFormatter.getFormattedRequest(query),
            mediaType,
            this@SearchViewModel
        )
    }

    override fun onLoadFinished(loader: Loader<List<SearchResult>>, data: List<SearchResult>?) {

        when (loader.id) {
            SEARCH_RESULT_LOADER -> resultList.value = data
        }
    }

    override fun onLoaderReset(loader: Loader<List<SearchResult>>) {
    }


    // ErrorCallback
    override fun onError(message: String) {

        Handler(Looper.getMainLooper()).post {
            iSearchActivity.showError(message)
        }
    }
}
