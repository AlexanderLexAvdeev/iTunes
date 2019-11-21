package com.regula.itunes.avdeevav.repository.loader

import androidx.loader.content.AsyncTaskLoader

import retrofit2.Response

import com.regula.itunes.avdeevav.App
import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.client.HttpClient
import com.regula.itunes.avdeevav.repository.data.SearchResult
import com.regula.itunes.avdeevav.repository.data.SearchResults


class SearchResultLoader(
        private val searchRequest: String,
        private val mediaType: String,
        private val errorCallback: ErrorCallback
) : AsyncTaskLoader<List<SearchResult>>(App.getContext()) {

    override fun onStartLoading() {

        super.onStartLoading()

        forceLoad()
    }

    override fun loadInBackground(): List<SearchResult>? {

        val response: Response<SearchResults>?

        try {
            response = HttpClient.getSearchService().getResult(searchRequest, mediaType).execute()
        } catch (e: Exception) {
            errorCallback.onError(context.resources.getString(R.string.noConnection))

            return null
        }

        response?.let {
            return if (it.isSuccessful) {
                it.body()?.results
            } else {
                errorCallback.onError(
                        "%s %s".format(context.resources.getString(R.string.connectionError), it.code())
                )
                null
            }
        } ?: return null
    }
}
