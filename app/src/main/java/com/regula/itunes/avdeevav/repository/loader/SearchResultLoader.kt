package com.regula.itunes.avdeevav.repository.loader

import androidx.loader.content.AsyncTaskLoader

import retrofit2.Response

import com.regula.itunes.avdeevav.App
import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.Favorites
import com.regula.itunes.avdeevav.repository.web.HttpClient
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

        response?.let { resp: Response<SearchResults> ->
            return if (resp.isSuccessful) {
                val favorites = Favorites()
                val searchResults: ArrayList<SearchResult>? = resp.body()?.results

                searchResults?.let { results: ArrayList<SearchResult> ->
                    favorites.setFavorites(results)
                }

                return searchResults
            } else {
                errorCallback.onError(
                        "%s %s".format(context.resources.getString(R.string.connectionError), response.code())
                )

                null
            }
        } ?: return null
    }
}
