package com.regula.itunes.avdeevav.repository.search.data

data class SearchResult(
        var trackId: Long? = -1,
        var artworkUrl100: String? = "",
        var trackName: String? = "",
        var artistName: String? = "",
        var kind: String? = "",
        var formattedPrice: String? = null,
        var trackPrice: Double? = null,
        var price: Double? = null,
        var currency: String? = "",
        var favorite: Boolean? = false
)
