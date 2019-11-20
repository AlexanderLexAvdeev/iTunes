package com.regula.itunes.avdeevav.view

import android.os.Handler
import android.os.Looper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import retrofit2.Response

import com.regula.itunes.avdeevav.repository.data.SearchResult
import com.regula.itunes.avdeevav.repository.data.SearchResults
import com.regula.itunes.avdeevav.repository.client.HttpClient

class SearchViewModel : ViewModel() {

    private var resultList: MutableLiveData<List<SearchResult>> = MutableLiveData()

    fun getResultListObservable() = resultList

    fun requestResult(query: String, mediaType: String) {

        Thread(Runnable {

            val searchRequest: String = query.replace(" ", "+")
            val response: Response<SearchResults> = HttpClient
                    .getSearchService()
                    .getResult(searchRequest, mediaType)
                    .execute()

            Handler(Looper.getMainLooper()).post {
                resultList.value = response.body()?.results
            }
        }).start()
    }
}
