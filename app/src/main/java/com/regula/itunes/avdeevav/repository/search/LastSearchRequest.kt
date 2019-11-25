package com.regula.itunes.avdeevav.repository.search

import android.content.Context

object LastSearchRequest {

    private const val LAST_SEARCH_REQUEST = "last_search_request"
    private const val QUERY = "query"
    private const val MEDIA_TYPE_INDEX = "mediaTypeIndex"
    private const val DEFAULT_NUMBER = 0
    private const val DEFAULT_STRING = ""

    fun getQuery(context: Context): String {

        return context
                .getSharedPreferences(LAST_SEARCH_REQUEST, Context.MODE_PRIVATE)
                .getString(
                        QUERY,
                        DEFAULT_STRING
                )
                ?: DEFAULT_STRING
    }

    fun setQuery(context: Context, query: String) {

        context
                .getSharedPreferences(LAST_SEARCH_REQUEST, Context.MODE_PRIVATE)
                .edit()
                .putString(QUERY, query)
                .apply()
    }

    fun getMediaTypeIndex(context: Context): Int {

        return context
                .getSharedPreferences(LAST_SEARCH_REQUEST, Context.MODE_PRIVATE)
                .getInt(
                        MEDIA_TYPE_INDEX,
                        DEFAULT_NUMBER
                )
    }

    fun setMediaTypeIndex(context: Context, mediaTypeIndex: Int) {

        context
                .getSharedPreferences(LAST_SEARCH_REQUEST, Context.MODE_PRIVATE)
                .edit()
                .putInt(MEDIA_TYPE_INDEX, mediaTypeIndex)
                .apply()
    }
}
