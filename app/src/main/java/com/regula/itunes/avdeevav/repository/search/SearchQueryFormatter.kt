package com.regula.itunes.avdeevav.repository.search

object SearchQueryFormatter {

    fun getFormattedRequest(query: String): String {

        return query.replace(" ", "+")
    }
}
