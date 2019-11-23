package com.regula.itunes.avdeevav.repository.search

enum class SearchMediaTypes(val mediaType: String, val value: String) {

    BOOKS("books", "ebook"),
    MUSIC("music", "music"),
    SOFTWARE("software", "software")
    // add new enum if needed
    // do not change positions of enums
}