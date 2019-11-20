package com.regula.itunes.avdeevav.repository.data

data class SearchResult(
        var trackId: Long? = -1,
        var artworkUrl100: String? = "",
        var trackName: String? = "",
        var artistName: String? = "",
        var kind: String? = "",
        var formattedPrice: String? = null,
        var trackPrice: Float? = 0.00f,
        var price: Float? = 0.00f,
        var currency: String? = "",
        var favorite: Boolean? = false
)
