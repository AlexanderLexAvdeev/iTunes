package com.regula.itunes.avdeevav.repository

object SearchQueryFormatter {

    fun getFormattedRequest(query: String): String {

        return query.replace(" ", "+")
    }
}
