package com.regula.itunes.avdeevav.repository.data

import io.realm.RealmObject

open class Favorite(
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
) : RealmObject()
