package com.regula.itunes.avdeevav.repository.search.loader

import android.os.Handler
import android.os.Looper
import retrofit2.Response

import com.regula.itunes.avdeevav.App
import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorage
import com.regula.itunes.avdeevav.repository.search.web.HttpClient
import com.regula.itunes.avdeevav.repository.search.data.SearchResult
import com.regula.itunes.avdeevav.repository.search.data.SearchResults

class SearchResultLoader(
        private val searchRequest: String,
        private val mediaType: String,
        private val searchResultLoaderCallback: SearchResultLoaderCallback
) : Thread() {

    override fun run() {

        val response: Response<SearchResults>?

        try {
            response = HttpClient.getSearchService().getResult(searchRequest, mediaType).execute()
        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post {
                searchResultLoaderCallback.onError(App.getContext().resources.getString(R.string.noConnection))
            }

            return
        }

        response?.let { resp: Response<SearchResults> ->
            if (resp.isSuccessful) {
                val favorites = FavoritesStorage()
                val searchResults: List<SearchResult>? = resp.body()?.results

                searchResults?.let { results: List<SearchResult> ->
                    favorites.setFavorites(results)
                }

                Handler(Looper.getMainLooper()).post {
                    searchResultLoaderCallback.onResult(searchResults)
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    searchResultLoaderCallback.onError(
                            "%s %s".format(App.getContext().resources.getString(R.string.connectionError), response.code())
                    )
                }
            }
        }
    }
}
