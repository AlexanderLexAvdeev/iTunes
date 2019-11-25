package com.regula.itunes.avdeevav.repository.search.loader

import android.widget.ImageView

import com.squareup.picasso.Picasso

object ImageLoader {

    fun load(fromUrl: String, intoView: ImageView) {

        Picasso.get().load(fromUrl).into(intoView)
    }
}
